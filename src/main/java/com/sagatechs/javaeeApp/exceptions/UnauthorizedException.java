package com.sagatechs.javaeeApp.exceptions;
public class UnauthorizedException extends GeneralAppException{

	private static final long serialVersionUID = 1L;

	public UnauthorizedException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnauthorizedException(String message) {
		super(message);
	}
}