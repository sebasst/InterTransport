package com.sagatechs.javaeeApp.web.security;

import java.util.Arrays;
import java.util.HashSet;

import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

//@ApplicationScoped
public class CustomIdentityStore //implements IdentityStore 
{

	//@Override
	public CredentialValidationResult validate(Credential credential) {
		UsernamePasswordCredential usernamePasswordCredential = (UsernamePasswordCredential) credential;

		CredentialValidationResult credentialValidationResult;

		if (usernamePasswordCredential.compareTo("admin", "admin")) {
			credentialValidationResult= new CredentialValidationResult("admin", new HashSet<>(Arrays.asList("ADMIN")));
		} else if (usernamePasswordCredential.compareTo("user", "user")) {
			credentialValidationResult= new CredentialValidationResult("user", new HashSet<>(Arrays.asList("USER")));
		} else {
			credentialValidationResult= CredentialValidationResult.INVALID_RESULT;
		}
		return credentialValidationResult;
	}

}
