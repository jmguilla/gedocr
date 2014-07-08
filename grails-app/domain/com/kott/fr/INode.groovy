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
		parents: ILink,
		children: ILink
	]
	
	static belongsTo = [
		owner: User,
		parents: Set
		]
	
	static mappedBy = [
		children: 'parent',
		parents: 'child'	
	]

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
			parents.each{result.addAll(it.parent.getPaths().collect{ it + pathSeparator + name})}
		}else{
			result = [pathSeparator + name]
		}
		result
	}
	
	void addToChildren(INode node){
		if(children == null){
			children = []
		}
		children.add(new ILink(parent: this, child: node));
	}
}
