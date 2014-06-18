package com.kott.fr

import grails.plugin.springsecurity.annotation.Secured


class RootController {

	@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
	def index() {
		render view: '/index.gsp'
	}
}
