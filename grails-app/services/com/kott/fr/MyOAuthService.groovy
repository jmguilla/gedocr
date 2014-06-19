package com.kott.fr

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.oauth.OAuthToken
import grails.plugin.springsecurity.userdetails.GormUserDetailsService
import grails.plugin.springsecurity.userdetails.GrailsUser

import javax.servlet.http.HttpSession

import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.context.SecurityContextHolder

class MyOAuthService {
	public OAuthToken updateOAuthToken(OAuthToken oAuthToken, User user) {
		def conf = SpringSecurityUtils.securityConfig

		// user

		String usernamePropertyName = conf.userLookup.usernamePropertyName
		String passwordPropertyName = conf.userLookup.passwordPropertyName
		String enabledPropertyName = conf.userLookup.enabledPropertyName
		String accountExpiredPropertyName = conf.userLookup.accountExpiredPropertyName
		String accountLockedPropertyName = conf.userLookup.accountLockedPropertyName
		String passwordExpiredPropertyName = conf.userLookup.passwordExpiredPropertyName

		String username = user."${usernamePropertyName}"
		String password = user."${passwordPropertyName}"
		boolean enabled = enabledPropertyName ? user."${enabledPropertyName}" : true
		boolean accountExpired = accountExpiredPropertyName ? user."${accountExpiredPropertyName}" : false
		boolean accountLocked = accountLockedPropertyName ? user."${accountLockedPropertyName}" : false
		boolean passwordExpired = passwordExpiredPropertyName ? user."${passwordExpiredPropertyName}" : false

		// authorities

		String authoritiesPropertyName = conf.userLookup.authoritiesPropertyName
		String authorityPropertyName = conf.authority.nameField
		Collection<?> userAuthorities = user."${authoritiesPropertyName}"
		def authorities = userAuthorities.collect { new GrantedAuthorityImpl(it."${authorityPropertyName}") }

		oAuthToken.principal = new GrailsUser(username, password, enabled, !accountExpired, !passwordExpired,
				!accountLocked, authorities ?: [GormUserDetailsService.NO_ROLE], user.id)
		oAuthToken.authorities = authorities
		oAuthToken.authenticated = true

		return oAuthToken
	}
	
	protected void authenticate(HttpSession session, OAuthToken oAuthToken) {
		session.removeAttribute OauthController.SPRING_SECURITY_OAUTH_TOKEN
		SecurityContextHolder.context.authentication = oAuthToken
	}
}
