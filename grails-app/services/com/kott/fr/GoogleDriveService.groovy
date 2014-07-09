package com.kott.fr

import grails.transaction.Transactional

import org.springframework.transaction.annotation.Propagation

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.About
import com.google.api.services.drive.model.FileList

@Transactional
class GoogleDriveService {

	@Transactional(propagation = Propagation.MANDATORY)
	def importAll(User owner) {
		def rootNodes = []
		HttpTransport httpTransport = new NetHttpTransport()
		JsonFactory jsonFactory = new JacksonFactory()
		GoogleCredential credential = new GoogleCredential().setAccessToken(owner.oAuthIDs[0].accessToken)
		Drive drive =  new Drive.Builder(httpTransport, jsonFactory, credential).build()
		About about = drive.about().get().execute();
		String rootFolderId = about.getRootFolderId()
		Drive.Files.List driveRequest = drive.files().list()
		driveRequest.setQ("trashed = false")
		FileList directories = null
		HashMap<String, Node> idToFile = new HashMap<String, Node>()
		while((directories = driveRequest.execute())){
			directories.getItems().each{
				def iNode = new INode(owner: owner, name: it.getTitle(), mimeType: it.getMimeType(), filesystemID: it.getId()).save(failOnError: true)
				owner.addToINodes(iNode)
				idToFile.put(it.getId(), new Node(null, [node: it, inode: iNode]))
			}
			if(!(driveRequest.setPageToken(directories.getNextPageToken()) && driveRequest.getPageToken() != null && driveRequest.getPageToken().length() > 0)){
				break;
			}
		}
		idToFile.keySet().each{key ->
			Node file = idToFile.get(key)
			if(file.name()["node"].getParents()){
				Node parent = idToFile.get(file.name()["node"].getParents()[0].getId())
				if(parent){
					file.setParent(parent)
					parent.name()["inode"].addToChildren(file.name()["inode"])
					parent.append(file)
				}
				if(rootFolderId.equals(file.name()["node"].getParents()[0].getId())){
					rootNodes << file.name()["inode"]
				}
			}
		}
		def saveRecurse = { INode inode, method ->
			inode.save(failOnError: true, flush: true)
			inode.children?.each{method(it, method)}
		}
		rootNodes.each{saveRecurse(it, saveRecurse)}
		owner.save(failOnError: true, flush: true)
	}
}
