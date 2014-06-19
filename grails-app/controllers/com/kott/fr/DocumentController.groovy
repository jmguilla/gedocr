package com.kott.fr

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

class DocumentController {
	
	def springSecurityService

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
				    HttpTransport httpTransport = new NetHttpTransport();
				    JsonFactory jsonFactory = new JacksonFactory();
					GoogleCredential credential = new GoogleCredential().setAccessToken(springSecurityService.currentUser.oAuthIDs[0].accessToken)
					Drive drive =  new Drive.Builder(httpTransport, jsonFactory, credential).build();
					com.google.api.services.drive.model.File body = new com.google.api.services.drive.model.File();
					body.setTitle(f.getOriginalFilename());
					body.setDescription("A test document");
					body.setMimeType(f.getContentType());
					Drive.Files.Insert driveRequest = drive.files().insert(body, mediaContent);
					driveRequest.getMediaHttpUploader().setProgressListener(new CustomProgressListener());
					driveRequest.execute();
					response.sendError(200, 'Done')
				}else{
					render view: 'upload'
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
