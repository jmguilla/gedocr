package com.kott.fr

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

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
import com.google.api.services.drive.model.FileList
import com.google.api.services.drive.model.ParentReference


class INodeController {

	def springSecurityService

	def static forTesting = null

	@Transactional
	@Secured(['IS_AUTHENTICATED_FULLY'])
	public directories(){
		withFormat{
			json{
				def rootNodes = INode.withCriteria {
					isEmpty("parents")
				}
//				if(!forTesting){
//					HttpTransport httpTransport = new NetHttpTransport()
//					JsonFactory jsonFactory = new JacksonFactory()
//					GoogleCredential credential = new GoogleCredential().setAccessToken(springSecurityService.currentUser.oAuthIDs[0].accessToken)
//					Drive drive =  new Drive.Builder(httpTransport, jsonFactory, credential).build()
//					About about = drive.about().get().execute();
//					String rootFolderId = about.getRootFolderId()
//					Drive.Files.List driveRequest = drive.files().list()
//					driveRequest.setQ("mimeType = 'application/vnd.google-apps.folder'")
//					FileList directories = null
//					HashMap<String, Node> idToFile = new HashMap<String, Node>()
//					while((directories = driveRequest.execute()) && driveRequest.setPageToken(directories.getNextPageToken())
//					&& driveRequest.getPageToken() != null && driveRequest.getPageToken().length() > 0){
//						directories.getItems().each{
//							idToFile.put(it.getId(), new Node(null, [node: it, inode: new INode(name: it.getTitle(), mimeType: "inode/directory", filesystemID: it.getId())]))
//						}
//					}
//					idToFile.keySet().each{key ->
//						Node file = idToFile.get(key)
//						if(file.name()["node"].getParents()){
//							Node parent = idToFile.get(file.name()["node"].getParents()[0].getId())
//							if(parent){
//								file.setParent(parent)
//								file.name()["inode"].addToParents(parent.name()["inode"])
//								parent.name()["inode"].addToChildren(file.name()["inode"])
//								parent.append(file)
//							}
//							if(rootFolderId.equals(file.name()["node"].getParents()[0].getId())){
//								rootNodes << file.name()["inode"]
//							}
//						}
//					}
//					forTesting = rootNodes
//					def saveRecurse = { INode inode, method ->
//						inode.save(failOnError: true)
//						inode.children?.each{method(it, method)}
//					}
//					rootNodes.each{saveRecurse(it, saveRecurse)}
//				}else{
//					rootNodes = forTesting
//				}
					
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
						flash.message = 'file cannot be empty'
						render(view: 'uploadForm')
						return
					}
					InputStreamContent mediaContent = new InputStreamContent(f.getContentType(), new BufferedInputStream(f.getInputStream()))
					mediaContent.setLength(f.getSize())
					HttpTransport httpTransport = new NetHttpTransport()
					JsonFactory jsonFactory = new JacksonFactory()
					GoogleCredential credential = new GoogleCredential().setAccessToken(springSecurityService.currentUser.oAuthIDs[0].accessToken)
					Drive drive =  new Drive.Builder(httpTransport, jsonFactory, credential).build()
					com.google.api.services.drive.model.File body = new com.google.api.services.drive.model.File()
					body.setTitle(f.getOriginalFilename())
					body.setDescription("A test document")
					body.setMimeType(f.getContentType())
					body.setParents(Arrays.asList(new ParentReference().setId("0B5DEy30M04E2S2hocVFERExfVms")))
					Drive.Files.Insert driveRequest = drive.files().insert(body, mediaContent)
					driveRequest.getMediaHttpUploader().setProgressListener(new CustomProgressListener())
					driveRequest.execute()
					flash.type = 'success'
					flash.message = 'upload successful'
				}
			}
		}
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
