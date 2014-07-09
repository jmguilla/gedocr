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

	static hasMany = [
		tags: Tag,
		parents: INode,
		children: INode
	]
	
	static belongsTo = [owner: User, parents: INode]
	
	static mappedBy = [children: 'parents',
		parents: 'children']
	
	static mapping = {
		id generator: 'uuid'
		parents joinTable: [name: "PARENT_CHILD", key: 'parent_id', column: 'child_id']
		children joinTable: [name: "CHILD_PARENT", key: 'child_id', column: 'parent_id']
	}

	@JsonApi(['directoriesWithPath'])
	Set tags

	@JsonApi(['directoriesWithPath'])
	Set children
	
	Set parents

	static constraints = {
		name nullable: false, blank: false
		mimeType nullable: false, blank: false, default: "UNKNOWN"
		parents nullable: true
		children nullable: true
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
