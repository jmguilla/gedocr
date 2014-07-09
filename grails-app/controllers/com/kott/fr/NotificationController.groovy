package com.kott.fr

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

class NotificationController {

	def springSecurityService

	@Secured(['IS_AUTHENTICATED_FULLY'])
	def query(ListCommand command){
		def notifications = Notification.withCriteria {
			and{
				eq('user', springSecurityService.getCurrentUser())
				eq('controller', command.c)
				eq('dismissed', false)
				or{
					eq('action', command.a)
					isNull('action')
				}
			}
		}
		withFormat{
			json{
				JSON.use('withoutUser'){ render(notifications as JSON) }
			}
		}
	}

	@Secured(['IS_AUTHENTICATED_FULLY'])
	def delete(){
		if(request.method == 'DELETE'){
			def result = []
			if(!params.id){
				response.status = 404
				result[type] = 'alert'
				result[message] = 'Missing id parameter'
			}else{
				def notification = Notification.get(params.id)
				if(!notification){
					response.status = 404
					result[type] = 'alert'
					result[message] = "No notification with such id ${params.id}"
				}else{
					notification.dismissed = true
					notification.save()
				}
			}
			withFormat{
				json{
					render(result as JSON)
					return
				}
			}
			response.status = 405
			render('Only json content type')
		}else{
			response.status = 405
			render('Only DELETE method allowed')
		}
	}
}

@grails.validation.Validateable
class ListCommand{
	String c
	String a
}
