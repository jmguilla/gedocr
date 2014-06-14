package com.kott.fr

import grails.events.Listener
import grails.transaction.Transactional

import org.springframework.transaction.annotation.Propagation

import com.kott.fr.User;
import com.kott.fr.UserRole;
import com.kott.fr.user.exceptions.CannotCreateUserException

@Transactional
class UserService {

  def messageSource
  
  @Transactional(propagation = Propagation.MANDATORY)
  def create(String email, String pwd) {
    User newUser = new User(email: email, password: pwd, enabled: false)
    UserRole newUserRole = new UserRole(user: newUser, role: Role.findByAuthority("ROLE_USER"))
    if(!newUser.save() || !newUserRole.save()){
      throw new CannotCreateUserException("Cannot create user", newUser, newUserRole)
    }
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
