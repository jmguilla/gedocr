package com.kott.fr

import grails.plugins.jsonapis.JsonApi

class INode {
	
	private static final String pathSeparator = '/'
	
	@JsonApi(['directoriesWithPath'])
	String name
	
	String filesystemID
	
	@JsonApi(['directoriesWithPath'])
	String mimeType

	static hasMany = [
		tags: Tag,
		parents: INode,
		children: INode
	]

	@JsonApi(['directoriesWithPath'])
	Set tags

	@JsonApi(['directoriesWithPath'])
	Set children

	static mappedBy = [parents: "children", children: "parents"]

	static constraints = {
		name nullable: false, blank: false
		mimeType nullable: false, blank: false, default: "UNKNOWN"
		parents nullable: true
		
		}
	
	static transients = ['paths']
	
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
	List<String> getPaths() {
		def result = []
		if(parents){
			parents.each{result.addAll(it.getPaths().collect{ it + pathSeparator + name})}
		}else{
			result = [pathSeparator + name]
		}
		result
	}
	
}