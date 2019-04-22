package com.sagatechs.javaeeApp.rest.security;

import javax.security.enterprise.credential.Credential;

public class UsernameJwtCredential implements Credential {

	private final String principal;
    private String token;

    /**
     * Constructor.
     *
     * @param callerName The caller name
     * @param password The token, as a String
     */
    public UsernameJwtCredential(String principal, String token) {
        this.principal = principal;
        this.token = token;
    }
    
    /**
     * Constructor.
     *
     * @param callerName The caller name
     * @param password The token, as a String
     */
    public UsernameJwtCredential(String token) {
        this.principal = "";
        this.token = token;
    }

    /**
     * Determines the password.
     * @return The password.
     */
    public String getToken() {
        return token;
    }
    
   
   
    public void clearCredential() {
        token=null;
    }

    public String getPrincipal() {
        return principal;
    }
    
    public boolean compareTo(String principal, String token) {
        return getPrincipal().equals(principal) && token.compareTo(token)==0;
    }

}