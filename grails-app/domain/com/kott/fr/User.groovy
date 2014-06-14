package com.kott.fr

class User {

  transient springSecurityService

  String email
  String username
  String password
  Date signin
  boolean enabled
  boolean accountExpired
  boolean accountLocked
  boolean passwordExpired
  
  static hasMany = [oAuthIDs: OAuthID]

  static transients = ['springSecurityService']

  static constraints = {
    email nullable: false, blank: false, unique: true, email: true
    username nullable: true, blank: false
    password blank: false
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
