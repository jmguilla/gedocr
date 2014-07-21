package com.kott.fr

import grails.transaction.Transactional

import org.springframework.transaction.annotation.Propagation

class NotificationService {
	
	def springSecurityService

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    def create(params) {
		params.user = springSecurityService.getCurrentUser()
		def notification = new Notification(params)
		if(!notification.save(flush: true)){
			log.warn("Cannot create notification with params $params because of ${notification.errors}")
			return
		}
    }
}
