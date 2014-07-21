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
						isEmpty("parents")
						isNull("owner")
						tags{
							idEq(tag.id)
						}
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
						flash.message = 'file cannot be empty'
						render(view: 'uploadForm')
						return
					}
					if(!params.selectedDirectory){
						flash.type = 'alert'
						flash.message = 'target directory not set'
						render view: 'upload'
					}
					INode targetDirectory = INode.get(JSON.parse(params.selectedDirectory).id)
					if(!targetDirectory){
						flash.type = 'alert'
						flash.message = 'No such target directory'
						render view: 'upload'
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
					body.setParents(Arrays.asList(new ParentReference().setId(targetDirectory.filesystemID)))
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
