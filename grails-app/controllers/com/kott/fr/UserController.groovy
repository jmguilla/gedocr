package com.kott.fr

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.plugin.springsecurity.oauth.OAuthToken
import grails.plugins.jsonapis.JsonApi
import grails.transaction.Transactional

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication


class UserController {

	static allowedMethods = [create: ['POST', 'GET'], update: 'POST', updatePWD: 'POST', confirmed: 'GET']

	def emailConfirmationService
	def authenticationManager
	def userService
	def springSecurityService
	def saltSource
	def myOAuthService
	def notificationService
	def googleDriveService

	@Secured(['IS_AUTHENTICATED_FULLY'])
	def accounts(){
		User user = springSecurityService.getCurrentUser()
		def oAuthIDs = user.oAuthIDs // force loading
		render view: 'accounts', model: [user: user]
	}
	/**
	 * Display view of user parameters
	 * @return
	 */
	@Secured(['IS_AUTHENTICATED_FULLY'])
	def show(){
		String view = 'show'
		respond(view: view)
	}

	/**
	 * Display view of user urls
	 * @return
	 */
	@Secured(['IS_AUTHENTICATED_FULLY'])
	def edit(){
		String view = 'edit'
		respond(view: view)
	}

	/**
	 * Return current user as JSON object
	 * TODO Check that method
	 * @return
	 */
	@Transactional
	@Secured(['IS_AUTHENTICATED_FULLY'])
	def getUser() {
		def result = [:]
		if(springSecurityService.isLoggedIn()){
			result.user = springSecurityService.getCurrentUser()
		}
		else{
			result.user = "not logged";
		}
		JSON.use(params.serialize?:'getUser'){ render(result as JSON) }
	}

	@Transactional
	@Secured(['permitAll'])
	def confirmed(){
		User user = User.findWhere(email: params.email)
		if(!user){
			return [uri:'/', args:[type: 'danger', message: message(code: 'user.confirmation.failure', default: 'No user with such email in our DB: {0}', args: [params.email])]]
		}else{
			user.enabled = true
			user.save(failOnError: true, flush: true)
			return [uri:'/', args:[type: 'success', message: message(code: 'user.confirmation.success', default: 'Thanks for having confirmed your email.')]]
		}
	}

	@Transactional()
	@Secured(['IS_AUTHENTICATED_FULLY'])
	def syncProvider(){
		withFormat{
			json{
				if(!params.provider){
					response.status = 404
					render([type: 'alert', message: message(code: 'user.provider.sync.missingparam', default: 'No provider in the request')] as JSON)
					return
				}
				notificationService.create(message: "Google drive synchronization has begun", controller: UserController.class.getSimpleName())
				googleDriveService.importAll()
				notificationService.create(message: "Google drive synchronization completed", controller: UserController.class.getSimpleName())
				render([type: 'success', message: 'import success'] as JSON)
			}
		}
	}

	/**
	 * Associates an OAuthID with an existing account. Needs the user's password to ensure
	 * that the user owns that account, and authenticates to verify before linking.
	 */
	def private linkAccount(OAuthLinkAccountCommand command){
		withFormat{
			html{
				redirect controller: 'OauthController', action: 'askToLinkOrCreateAccount()'
			}
			json{
				OAuthToken oAuthToken = session[OauthController.SPRING_SECURITY_OAUTH_TOKEN]
				assert oAuthToken, "There is no auth token in the session!"
				def result = [:]

				if (request.post) {
					boolean linked = command.validate() && User.withTransaction { status ->
						UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(command.email, command.password);
						Authentication auth = null
						User user = null
						try{
							auth = authenticationManager.authenticate(token)
							user = User.findByEmail(command.email)
						}catch(BadCredentialsException bce){
							//miam miam -> wrong auth
						}
						if(user && auth?.isAuthenticated()){
							user.addToOAuthIDs(provider: oAuthToken.providerName, accessToken: oAuthToken.accessToken.token, user: user)
							if (user.validate() && user.save()) {
								oAuthToken = myOAuthService.updateOAuthToken(oAuthToken, user)
								result['type'] = 'success'
								result['message'] = message(code: 'user.oauth.link.success', default: 'Accounts properly linked')
								return true
							}
						} else {
							response.status = 406
							command.errors.rejectValue("email", "OAuthLinkAccountCommand.email.not.exists")
							result['type'] = 'danger'
							result['message'] = message(code: 'user.oauth.link.failure', default: 'Cannot link accounts')
							result['command'] = command
						}

						status.setRollbackOnly()
						return false
					}

					if (linked) {
						myOAuthService.authenticate(session, oAuthToken)
					}
					render result as JSON
					return
				}else{
					response.status = 405
					return
				}
			}
		}
		response.status = 406
	}

	def private create(UserRegistrationCommand command){
		withFormat{
			html{
				//will render create.gsp
				respond(view: 'create')
				return
			}
			json{
				JSON.use("userUpdate"){
					def result = null
					if(request.post){
						User newUser = null
						if(command.validate() && !(newUser = userService.create(command.properties))?.hasErrors()){
							// treating the case when the user creation is done after oauth authentication
							// TODO check email confirmation in both cases
							OAuthToken oAuthToken = session[OauthController.SPRING_SECURITY_OAUTH_TOKEN]
							if(oAuthToken){
								newUser.enabled = true
								newUser.addToOAuthIDs(provider: oAuthToken.providerName, accessToken: oAuthToken.accessToken.token, user: newUser)
								if (newUser.validate() && newUser.save()) {
									oAuthToken = myOAuthService.updateOAuthToken(oAuthToken, newUser)
									myOAuthService.authenticate(session, oAuthToken)
								}
							}else{
								// we only need confirmation for enabling the account if no oauth
								emailConfirmationService.sendConfirmation(
										from: message(code: 'user.create.email.from'),
										to: newUser.email,
										subject: message(code: 'user.create.email.title'))
							}
							result = [type: 'success', message: message(code: 'user.create.success', default: 'User created!!'), user: newUser]
						}else{
							response.status = 406
							newUser?.getErrors()?.getAllErrors()?.each{command.errors.addError(it)}
							result = [
								type: 'danger',
								message: message(code: "user.create.failure"),
								user: command
							]
						}
					}else{
						response.status = 405
					}
					render(result as JSON)
				}
			}
		}
	}

	/**
	 * To update a user's details. So far, only "username" can be updated.
	 * curl -X POST -d "{'username': 'monusername'}" -> since authentication is required, doesn't work with curl...
	 * 
	 * @return
	 */
	@Secured(['IS_AUTHENTICATED_FULLY'])
	@Transactional
	def update(){
		User me = springSecurityService.getCurrentUser()
		bindData(me, request.JSON, [include: ['username']])
		me.save(failOnError: true, flush: true)
		JSON.use('userUpdate'){
			render ( [type: 'success', message: message(code: 'user.update.success', default: 'Update performed successfully'), user: me] as JSON)
		}
	}
}

@grails.validation.Validateable
class UserRegistrationCommand {
	String username
	String email
	String emailConfirmation
	String password
	String passwordConfirmation

	static constraints = {
		importFrom User, include: ["username", "email", "password"]
		emailConfirmation blank: false, validator: { val, UserRegistrationCommand obj -> obj.email.equals(val)}
		passwordConfirmation blank: false, validator: { val, UserRegistrationCommand obj -> obj.password.equals(val)}
	}
}

@grails.validation.Validateable
class OAuthLinkAccountCommand {
	String email
	String password

	static constraints = {
		importFrom User, include: ["email", "password"]
	}
}

@grails.validation.Validateable
class UpdatePWDCommand {
	transient def springSecurityService
	transient def saltSource
	@JsonApi("userUpdate")
	String password
	@JsonApi("userUpdate")
	String newPassword
	@JsonApi("userUpdate")
	String newPasswordConfirmation

	static marshalling={
		json{
			userUpdate{
				shouldOutputIdentifier false
				shouldOutputVersion false
				shouldOutputClass false
			}
		}
	}

	static transients = ['springSecurityService']

	static constraints = {
		password blank: false, validator: {val, UpdatePWDCommand obj ->
			def user = obj.springSecurityService.getCurrentUser()
			def userDetails = obj.springSecurityService.userDetailsService.loadUserByUsername(user.email)
			def salt = obj.saltSource.getSalt(userDetails)
			if(!obj.springSecurityService.passwordEncoder.isPasswordValid(userDetails.password, obj.password, salt)){
				return ['updatepwdcommand.password.invalid']
			}
		}
		newPassword blank: false
		newPasswordConfirmation blank: false, validator: {val, UpdatePWDCommand obj ->
			if(!val.equals(obj.newPassword)){
				return ['updatepwdcommand.newpasswordconfirmation.invalid']
			}
		}
	}
}