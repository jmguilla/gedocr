package com.kott.fr
import grails.plugins.jsonapis.JsonApi


class Notification {

	User user
	
	@JsonApi(['withoutUser'])
	String message

	String controller

	String action
	
	@JsonApi(['withoutUser'])
	Date creation

	boolean dismissed

	static belongsTo = User

	static constraints = {
		controller nullable: true
		action nullable: true
	}
	
	static marshalling={
		withoutUser{
		  directoriesWithPath{
			shouldOutputIdentifier false
			shouldOutputVersion false
			shouldOutputClass false
		  }
		}
	  }

	def beforeValidate() {
		if(!creation){
			creation = new Date()
		}
	}
}
