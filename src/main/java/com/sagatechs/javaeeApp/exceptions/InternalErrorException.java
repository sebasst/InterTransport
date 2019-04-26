package com.sagatechs.javaeeApp.exceptions;
public class InternalErrorException extends RuntimeException {

	private static final long serialVersionUID = 6354303826163403006L;

	public InternalErrorException() {
		super("Internal error");

	}

}