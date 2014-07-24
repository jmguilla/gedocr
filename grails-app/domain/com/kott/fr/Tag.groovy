package com.kott.fr

import grails.plugins.jsonapis.JsonApi

class Tag {
	
	@JsonApi(['directoriesWithPath'])
	String value

	boolean publique = false

	static constraints = {
		value nullable: false, unique: true, size: 1..255
		publique nullable: false
	}

	static marshalling={
		json{
			directoriesWithPath{
				shouldOutputIdentifier false
				shouldOutputVersion false
				shouldOutputClass false
			}
		}
	}
}
