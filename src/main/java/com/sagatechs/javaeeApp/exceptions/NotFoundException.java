package com.sagatechs.javaeeApp.exceptions;
public class NotFoundException extends RuntimeException{

	private static final long serialVersionUID = -5352821283292887644L;
	
	public NotFoundException(String message) {
		
		super(message);
	}

}