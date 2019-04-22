package com.sagatechs.javaeeApp.exceptions;

import javax.ejb.ApplicationException;

//https://stackoverflow.com/questions/19563088/a-clear-explanation-of-system-exception-vs-application-exception
@ApplicationException(rollback=true,inherited=true)
public class GeneralAppException extends Exception{

	private static final long serialVersionUID = 1L;

	public GeneralAppException(String message, Throwable cause) {
		super(message, cause);
	}

	public GeneralAppException(String message) {
		super(message);
	}
}
