package com.kott.fr.user.exceptions

import com.kott.fr.User
import com.kott.fr.UserRole

class CannotCreateUserException extends RuntimeException {
	String message
	User user
	UserRole userRole

	CannotCreateUserException(String message, User user, UserRole userRole){
		super(message)
		this.user = user
		this.userRole = userRole
	}
}
