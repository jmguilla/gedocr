package com.kott.fr

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.GrantedAuthorityImpl

import com.the6hours.grails.springsecurity.facebook.FacebookAuthToken

import facebook4j.Facebook
import facebook4j.FacebookFactory
import facebook4j.auth.AccessToken
import grails.transaction.Transactional

@Transactional
class FacebookAuthService {

	def mailService
	def messageSource

	def  Collection<GrantedAuthority> getRoles(User user){
		user.getAuthorities().collect { new GrantedAuthorityImpl(it.authority) } as Set
	}

	def createRoles(User fbUser){
		//does nothing, hihi
	}

	//TODO centralise user creation
	def User createAppUser(User fbUser, FacebookAuthToken token) {
		Facebook facebook = new FacebookFactory().getInstance();
		facebook.setOAuthAccessToken(new AccessToken(token.accessToken.accessToken, null));
		def fbOAuth = facebook.getMe()
		User user = User.findWhere(email: fbOAuth.email)
		if(!user){
			user = new User()
			user.signin = new Date()
			user.username = fbOAuth.username
			// TODO compute default random password
			user.password = "94bc790da2e2"
			user.email = fbOAuth.email
			user = user.save(flush: true, failOnError: true)
			UserRole.create(user, Role.findByAuthority('ROLE_USER'), true)
			UserRole.create(user, Role.findByAuthority('ROLE_FACEBOOK'), true)
			assert UserRole.findAllByUser(user).size() == 2
			mailService.sendMail {
				to user.email
				subject messageSource.getMessage('user.welcome.subject',[user.firstName + " " + user.lastName].toArray(), 'Welcome!', new Locale('fr'))
				body messageSource.getMessage('user.defaultpwd',[password, "http://change.me"].toArray(), 'A new account has been created for you and linked to your Facebook account.' +
				'A default password: $password has also been set. Change it as soon as possible!', new Locale('fr'))}
		}else{
			//TODO put that after fb user creation
			int size = UserRole.findAllByUser(user).size()
			UserRole.create(user, Role.findByAuthority('ROLE_FACEBOOK'), true)
			assert UserRole.findAllByUser(user).size() == size + 1
			mailService.sendMail {
				to user.email
				subject messageSource.getMessage('user.welcome.subject', [user.firstName + " " + user.lastName].toArray(), 'Welcome!', new Locale('fr'))
				body messageSource.getMessage('user.fblinked', [].toArray(), 'Your Facebook account has been linked to your existing account.', new Locale('fr'))}
		}
		user
	}
}
