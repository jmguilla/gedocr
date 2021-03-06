package com.kott.fr

import grails.plugins.jsonapis.JsonApi

class User {

	transient springSecurityService
	
	@JsonApi(['userUpdate', 'getUser', 'withOAuthIDs'])
	String email
	@JsonApi(['userUpdate', 'getUser', 'withOAuthIDs'])
	String username
	String password = "pwd"
	Date signin
	boolean enabled
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
	Config configuration
	
	@JsonApi(['withOAuthIDs'])
	Set oAuthIDs
	
	static marshalling={
		json{
		  userUpdate{
			shouldOutputIdentifier false
			shouldOutputVersion false
			shouldOutputClass false
		  }
		  getUser{
			shouldOutputIdentifier false
			shouldOutputVersion false
			shouldOutputClass false
		  }
		  withOAuthIDs{
			shouldOutputIdentifier false
			shouldOutputVersion false
			shouldOutputClass false
		  }
		}
	  }

	static hasMany = [
		oAuthIDs: OAuthID,
		iNodes: INode
		]
	
	static transients = ['springSecurityService']

	static constraints = {
		email nullable: false, blank: false, unique: true, email: true
		username nullable: false, blank: false
		password nullable: true
		signin nullable: false, blank: false
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}

	def beforeValidate() {
		if(!signin){
			signin = new Date()
		}
		if(!configuration){
			configuration = new Config()
			configuration.user = this
			configuration = configuration.save()
		}
	}
}
