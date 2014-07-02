package com.kott.fr

import grails.plugin.springsecurity.annotation.Secured

class DocumentationController {

	def index() {
	}

	@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
	def description() {
		String view = "description"
		render view : view
	}

	@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
	def terms() {
		String view = "terms"
		render view : view
	}

	@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
	def faq() {
		String view = "faq"
		render view : view
	}
}
