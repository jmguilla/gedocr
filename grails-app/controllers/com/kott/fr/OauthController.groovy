/*
 * Copyright 2012 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kott.fr

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.oauth.OAuthToken
import grails.plugin.springsecurity.userdetails.GrailsUser

import javax.annotation.security.PermitAll

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

/**
 * Simple helper controller for handling OAuth authentication and integrating it
 * into Spring Security.
 */
class OauthController {

	public static final String SPRING_SECURITY_OAUTH_TOKEN = 'springSecurityOAuthToken'

	def grailsApplication
	def oauthService
	def userService
	def springSecurityService
	def myOAuthService

	/**
	 * This can be used as a callback for a successful OAuth authentication
	 * attempt. It logs the associated user in if he or she has an internal
	 * Spring Security account and redirects to <tt>targetUri</tt> (provided as a URL
	 * parameter or in the session). Otherwise it redirects to a URL for
	 * linking OAuth identities to Spring Security accounts. The application must implement
	 * the page and provide the associated URL via the <tt>oauth.registration.askToLinkOrCreateAccountUri</tt>
	 * configuration setting.
	 */
	@PermitAll
	def success () {
		// Validate the 'provider' URL. Any errors here are either misconfiguration
		// or web crawlers (or malicious users).
		if (!params.provider) {
			renderError 400, "The Spring Security OAuth callback URL must include the 'provider' URL parameter."
			return
		}

		def sessionKey = oauthService.findSessionKeyForAccessToken(params.provider)
		if (!session[sessionKey]) {
			renderError 500, "No OAuth token in the session for provider '${params.provider}'!"
			return
		}

		// Create the relevant authentication token and attempt to log in.
		OAuthToken oAuthToken = createAuthToken(params.provider, session[sessionKey])

		if (!(oAuthToken.principal instanceof GrailsUser)) {
			// This OAuth account hasn't been registered against an internal
			// account yet. Give the oAuthID the opportunity to create a new
			// internal account or link to an existing one.
			User newUser = userService.create([email: oAuthToken.principal, username: oAuthToken.principal])
			newUser.enabled = true
			newUser.addToOAuthIDs(provider: oAuthToken.providerName, accessToken: oAuthToken.accessToken.token, user: newUser)
			oAuthToken = myOAuthService.updateOAuthToken(oAuthToken, newUser)
			myOAuthService.authenticate(session, oAuthToken)

		}
		authenticateAndRedirect(oAuthToken, defaultTargetUrl)
	}

	@PermitAll
	def failure () {
		authenticateAndRedirect(null, defaultTargetUrl)
	}

	def private askToLinkOrCreateAccount() {
		if (springSecurityService.loggedIn) {
			def currentUser = springSecurityService.currentUser
			OAuthToken oAuthToken = session[SPRING_SECURITY_OAUTH_TOKEN]
			assert oAuthToken, "There is no auth token in the session!"
			currentUser.addToOAuthIDs(provider: oAuthToken.providerName, accessToken: oAuthToken.socialId, user: currentUser)
			if (currentUser.validate() && currentUser.save()) {
				oAuthToken = myOAuthService.updateOAuthToken(oAuthToken, currentUser)
				authenticateAndRedirect(oAuthToken, defaultTargetUrl)
				return
			}
		}
	}

	protected renderError(code, msg) {
		log.error msg + " (returning ${code})"
		render status: code, text: msg
	}

	protected OAuthToken createAuthToken(providerName, scribeToken) {
		def providerService = grailsApplication.mainContext.getBean("${providerName}SpringSecurityOAuthService")
		OAuthToken oAuthToken = providerService.createAuthToken(scribeToken)

		def oAuthID = OAuthID.find("from OAuthID as a where a.provider=:provider and a.user.email = :email", [provider: providerName, email: oAuthToken.socialId])
		if (oAuthID) {
			myOAuthService.updateOAuthToken(oAuthToken, oAuthID.user)
			// taking care of updating the access_token
			oAuthID.accessToken = oAuthToken.accessToken.token
			oAuthID.save()
		}

		return oAuthToken
	}

	protected Map getDefaultTargetUrl() {
		def config = SpringSecurityUtils.securityConfig
		def savedRequest = SpringSecurityUtils.getSavedRequest(session)
		def defaultUrlOnNull = '/'

		if (savedRequest && !config.successHandler.alwaysUseDefault) {
			return [url: (savedRequest.redirectUrl ?: defaultUrlOnNull)]
		} else {
			return [uri: (config.successHandler.defaultTargetUrl ?: defaultUrlOnNull)]
		}
	}

	protected void authenticate(OAuthToken oAuthToken) {
		session.removeAttribute SPRING_SECURITY_OAUTH_TOKEN
		SecurityContextHolder.context.authentication = oAuthToken
	}

	protected void authenticateAndRedirect(OAuthToken oAuthToken, redirectUrl) {
		myOAuthService.authenticate(session, oAuthToken)
		redirect(redirectUrl instanceof Map ? redirectUrl : [uri: redirectUrl])
	}

}

class OAuthCreateAccountCommand {

	String email
	String password1
	String password2

	static constraints = {
		email blank: false, validator: { String email, command ->
			User.withNewSession { session ->
				if (email && User.countByEmail(email)) {
					return 'OAuthCreateAccountCommand.email.error.unique'
				}
			}
		}
		password1 blank: false, minSize: 8, maxSize: 64, validator: { password1, command ->
			if (command.email && command.email.equals(password1)) {
				return 'OAuthCreateAccountCommand.password.error.email'
			}

			if (password1 && password1.length() >= 8 && password1.length() <= 64 &&
			(!password1.matches('^.*\\p{Alpha}.*$') ||
			!password1.matches('^.*\\p{Digit}.*$'))) {
				return 'OAuthCreateAccountCommand.password.error.strength'
			}
		}
		password2 nullable: true, blank: true, validator: { password2, command ->
			if (command.password1 != password2) {
				return 'OAuthCreateAccountCommand.password.error.mismatch'
			}
		}
	}
}
