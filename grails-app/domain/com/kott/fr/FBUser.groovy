package com.kott.fr

import com.kott.fr.User

class FBUser {

	long uid
	String accessToken
	Date accessTokenExpires

	static belongsTo = [user: User]

	static constraints = { uid unique: true }
}
