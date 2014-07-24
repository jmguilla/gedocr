package com.kott.fr

import grails.plugins.jsonapis.JsonApi


class INode {
	
	private static final String pathSeparator = '/'
	
	String id
	
	@JsonApi(['directoriesWithPath'])
	String name
	
	String filesystemID
	
	@JsonApi(['directoriesWithPath'])
	String mimeType
	
	INode parent
	
	User owner

	static hasMany = [
		tags: Tag,
		children: INode
	]
	
	static belongsTo = [owner: User, parent: INode]
	
	static mappedBy = [children: 'parent',
		parent: 'children']
	
	static mapping = {
		id generator: 'uuid'
	}

	@JsonApi(['directoriesWithPath'])
	Set tags

	@JsonApi(['directoriesWithPath'])
	Set children
	
	static constraints = {
		owner nullable: true
		name nullable: false, blank: false
		mimeType nullable: false, blank: false, default: "UNKNOWN"
		parent nullable: true
		children nullable: true
		}
	
	static transients = ['path']
	
	static marshalling={
		json{
		  directoriesWithPath{
			shouldOutputIdentifier false
			shouldOutputVersion false
			shouldOutputClass false
		  }
		}
	  }
	
	@JsonApi(['directoriesWithPath'])
	String getPath() {
		def result = null
		if(parent){
			result = parent.getPath() + pathSeparator + name
		}else{
			result = pathSeparator + name
		}
		result
	}
}
