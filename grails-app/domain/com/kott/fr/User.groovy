package com.kott.fr

import grails.plugins.jsonapis.JsonApi

import org.springframework.validation.Errors

class User {

	transient springSecurityService

	@JsonApi("userUpdate")
	String email
	@JsonApi("userUpdate")
	String username
	String password
	Date signin
	boolean enabled
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
	
	static marshalling={
		json{
		  userUpdate{
			shouldOutputIdentifier false
			shouldOutputVersion false
			shouldOutputClass false
		  }
		}
	  }

	static hasMany = [oAuthIDs: OAuthID]

	static transients = ['springSecurityService']

	static constraints = {
		email nullable: false, blank: false, unique: true, email: true
		username nullable: false, blank: false
		password nullable: false, blank: false
		signin nullable: false, blank: false
	}

	static mapping = { password column: '`password`' }

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	def beforeValidate() {
		if(!signin){
			signin = new Date()
		}
	}

	protected void encodePassword() {
		password = springSecurityService.encodePassword(password)
	}
}
