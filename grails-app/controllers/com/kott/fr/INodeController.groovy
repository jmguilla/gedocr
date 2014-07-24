package com.kott.fr

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

import org.springframework.transaction.annotation.Propagation
import org.springframework.web.multipart.MultipartFile

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.media.MediaHttpUploader
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener
import com.google.api.client.googleapis.media.MediaHttpUploader.UploadState
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.InputStreamContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.About
import com.google.api.services.drive.model.ParentReference


class INodeController {

	def springSecurityService

	@Transactional
	@Secured(['IS_AUTHENTICATED_FULLY'])
	public directories(){
		withFormat{
			json{
				def tag = Tag.findByValue("SmartFolder-0.1")
				def rootNodes = INode.withCriteria {
					and{
						isNull("parent")
						isNull("owner")
						tags{ idEq(tag.id) }
						eq("mimeType", 'inode/directory')
					}
				}

				JSON.use("directoriesWithPath"){
					render([type: 'success', message: 'Here are your directdories', result: rootNodes] as JSON)
				}
			}
			'*'{
				response.status = 406
				render([type: 'danger', message: 'Serving only json'] as JSON)
			}
		}
	}

	@Transactional
	@Secured(['IS_AUTHENTICATED_FULLY'])
	public upload(){
		withFormat{
			html{
				if(request.post){
					// the form is submitting
					MultipartFile f = request.getFile('document')
					if (f.empty) {
						flash.type = 'danger'
						flash.message = 'file cannot be empty'
						render(view: 'upload')
						return
					}
					if(!params.selectedDirectory){
						flash.type = 'danger'
						flash.message = 'target directory not set'
						render view: 'upload'
					}
					INode targetDirectory = INode.get(JSON.parse(params.selectedDirectory).id)
					if(!targetDirectory){
						flash.type = 'danger'
						flash.message = 'No such target directory'
						render view: 'upload'
					}
					HttpTransport httpTransport = new NetHttpTransport()
					JsonFactory jsonFactory = new JacksonFactory()
					GoogleCredential credential = new GoogleCredential().setAccessToken(springSecurityService.currentUser.oAuthIDs[0].accessToken)
					Drive drive =  new Drive.Builder(httpTransport, jsonFactory, credential).build()
					INode tmp = targetDirectory
					def reverseParents = []
					while(tmp){
						reverseParents << tmp
						tmp = tmp.parent
					}
					INode parent = retrieveOrCreateDirectory(null, springSecurityService.currentUser.configuration.destinationFolder);
					reverseParents = reverseParents.reverse()
					reverseParents.each{
						parent = retrieveOrCreateDirectory(parent, it.name);
					}
					com.google.api.services.drive.model.File body = new com.google.api.services.drive.model.File()
					body.setTitle(f.getOriginalFilename())
					body.setDescription("A test document")
					body.setMimeType(f.getContentType())
					InputStreamContent mediaContent = new InputStreamContent(f.getContentType(), new BufferedInputStream(f.getInputStream()))
					mediaContent.setLength(f.getSize())
					body.setParents(Arrays.asList(new ParentReference().setId(parent.filesystemID)))
					Drive.Files.Insert driveRequest = drive.files().insert(body, mediaContent)
					com.google.api.services.drive.model.File result = driveRequest.execute()
					new INode(filesystemID: result.getId(), parent: parent, name: f.getOriginalFilename(), mimeType: f.getContentType(), owner: springSecurityService.currentUser).save(failOnError: true, flush: true)
					flash.type = 'success'
					flash.message = 'upload successful'
				}
			}
		}
	}

	/**
	 * Retrieves the INode if it exists, creates a new directory and returns it otherwise
	 * @param parent
	 * @param rootDirectoryName
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	def private INode retrieveOrCreateDirectory(INode parent, String name){
		if(parent && !parent.isAttached()){
			parent.attach()
		}
		INode iNode = null
		if(!parent){
			iNode = INode.find("from INode as inode where inode.parent is null and inode.owner=:owner and inode.name=:name",
					[owner: springSecurityService.currentUser, name: name])
		}else{
			iNode = INode.find("from INode as inode where inode.parent=:parent and inode.owner=:owner and inode.name=:name",
					[parent: parent, owner: springSecurityService.currentUser, name: name])
		}
		if(!iNode){
			iNode = createDirectory(parent, name)
			if(iNode){
				iNode = iNode.save(failOnError: true, flush: true)
			}else{
				throw new IllegalStateException("Cannot retrieve/create directory: $name in $parent")
			}
		}
		iNode
	}

	def private INode createDirectory(INode parent, String name){
		HttpTransport httpTransport = new NetHttpTransport()
		JsonFactory jsonFactory = new JacksonFactory()
		GoogleCredential credential = new GoogleCredential().setAccessToken(springSecurityService.currentUser.oAuthIDs[0].accessToken)
		Drive drive =  new Drive.Builder(httpTransport, jsonFactory, credential).build()
		String parentId = null
		if(!parent){
			About about = drive.about().get().execute();
			parentId = about.getRootFolderId()
		}else{
			parentId = parent.filesystemID
		}
		com.google.api.services.drive.model.File body = new com.google.api.services.drive.model.File()
		body.setTitle(name)
		body.setDescription("directory")
		body.setMimeType("application/vnd.google-apps.folder")
		body.setParents(Arrays.asList(new ParentReference().setId(parentId)))
		Drive.Files.Insert driveRequest = drive.files().insert(body)
		com.google.api.services.drive.model.File result = driveRequest.execute()
		new INode(owner: springSecurityService.currentUser, name: name, filesystemID: result.getId(), mimeType: 'inode/directory', parent: parent)
	}
}

class CustomProgressListener implements MediaHttpUploaderProgressListener {
	public void progressChanged(MediaHttpUploader uploader) throws IOException {
		switch (uploader.getUploadState()) {
			case UploadState.INITIATION_STARTED:
				System.out.println("Initiation has started!");
				break;
			case UploadState.INITIATION_COMPLETE:
				System.out.println("Initiation is complete!");
				break;
			case UploadState.MEDIA_IN_PROGRESS:
				System.out.println(uploader.getProgress());
				break;
			case UploadState.MEDIA_COMPLETE:
				System.out.println("Upload is complete!");
		}
	}
}
