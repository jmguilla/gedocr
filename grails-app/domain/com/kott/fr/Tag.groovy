package com.kott.fr

class Tag {

	String value

    static constraints = {
		value nullable: false, unique: true, size: 1..255
    }
}
