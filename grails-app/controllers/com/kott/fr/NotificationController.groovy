package com.kott.fr

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

class NotificationController {
	
	def springSecurityService

	@Secured(['IS_AUTHENTICATED_FULLY'])
    def get(GetCommand command){
		def notifications = Notification.withCriteria {
			and{
				eq('user', springSecurityService.getCurrentUser())
				eq('controller', command.controller)
				eq('dismissed', false)
				or{
					eq('action', command.action)
					isNull('action')
				}
			}
		}
		withFormat{
			json{
				JSON.use('deep'){
					render([type: 'info', message: 'notifications', notifications: notifications] as JSON)
				}
			}
		}
	}
}

@grails.validation.Validateable
class GetCommand{
	String controller
	String action
}
