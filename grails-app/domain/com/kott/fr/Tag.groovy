package com.kott.fr

class Tag {

	String value
	
	boolean publique = false

    static constraints = {
		value nullable: false, unique: true, size: 1..255
		publique nullable: false
    }
}
