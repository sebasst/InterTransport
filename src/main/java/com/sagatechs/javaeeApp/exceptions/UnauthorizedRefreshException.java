package com.sagatechs.javaeeApp.exceptions;
public class UnauthorizedRefreshException extends GeneralAppException{

	private static final long serialVersionUID = 1L;

	public UnauthorizedRefreshException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnauthorizedRefreshException(String message) {
		super(message);
	}
}