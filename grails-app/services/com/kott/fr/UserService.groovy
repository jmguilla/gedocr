package com.kott.fr

import grails.events.Listener
import grails.transaction.Transactional

import org.codehaus.groovy.grails.web.json.JSONObject
import org.springframework.transaction.annotation.Propagation

import com.kott.fr.user.exceptions.CannotCreateUserException

@Transactional
class UserService {

  def messageSource
  
  @Transactional(propagation = Propagation.MANDATORY)
	def create(Map map) {
    User newUser = new User(map)
    UserRole newUserRole = new UserRole(user: newUser, role: Role.findByAuthority("ROLE_USER"))
    newUser.save() && newUserRole.save()
    return newUser
  }
  
  @Listener(topic= 'confirmed', namespace= 'plugin.emailConfirmation')
  def userConfirmed(info){
    log.info "User ${info.email} successfully confirmed with application id data ${info.id}"
    return [controller: 'user', action: 'confirmed', params: info]
  }

  @Listener(topic='timeout', namespace='plugin.emailConfirmation')
  def userConfirmationTimedOut(info) {
    log.info "A user failed to confirm, the token in their link was ${info.token}"
  }
}
