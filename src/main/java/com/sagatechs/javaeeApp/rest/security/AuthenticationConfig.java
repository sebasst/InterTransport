package com.sagatechs.javaeeApp.rest.security;
public class AuthenticationConfig {

	public static final int DENIED_STATUS_CODE = 401;
	public static final int FORBIDDEN_STATUS_CODE = 403;
	public static final String DENIED_STATUS_MESSAGE = "Unauthorized!";
	public static final String FORBIDDEN_STATUS_MESSAGE = "Forbidden!";
	public static final String DENIED_STATUS_DETAIL = "Only valid user access the resource.";
	public static final String FORBIDDEN_STATUS_DETAIL = "Access blocked for all users ";
}