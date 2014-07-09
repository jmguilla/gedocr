package com.kott.fr

class Notification {

	User user

	String message

	String controller

	String action

	Date creation

	boolean dismissed
	
	static belongsTo = User

	static constraints = {
		controller nullable: true
		action nullable: true
	}

	def beforeInsert(){
		if(!creation){
			creation = new Date()
		}
	}
}
