package com.kott.fr

class GoogleAuthTagLib {
	static namespace = "googleAuth"

	def connect = {attrs, body ->
	   out << body() << ("test")
	}
}
