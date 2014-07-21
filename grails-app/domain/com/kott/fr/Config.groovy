package com.kott.fr

class Config {
	
	String destinationFolder
	
	static belongsTo = [user: User]

    static constraints = {
		destinationFolder nullable: false, blank: false
    }
	
	def beforeValidate(){
		if(!destinationFolder){
			destinationFolder = 'gedocr'
		}
	}
}
