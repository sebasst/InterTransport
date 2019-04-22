package com.sagatechs.javaeeApp.web.security;

import java.util.EnumSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

import com.sagatechs.javaeeApp.rest.security.UserSecurityService;

@ApplicationScoped
public class UsernamePasswordIdentityStore implements IdentityStore {

	@Inject
	UserSecurityService userSecurityService;

	@Override
	public CredentialValidationResult validate(Credential credential) {
		CredentialValidationResult credentialValidationResult;
		if (credential instanceof UsernamePasswordCredential) {
			UsernamePasswordCredential usernamePasswordCredential = (UsernamePasswordCredential) credential;

			

			if (this.userSecurityService.authenticateUsernamePassword(usernamePasswordCredential.getCaller(),
					usernamePasswordCredential.getPasswordAsString())) {
				credentialValidationResult = new CredentialValidationResult(usernamePasswordCredential.getCaller());
			} else {
				credentialValidationResult = CredentialValidationResult.INVALID_RESULT;
			}
		}else {
			credentialValidationResult = CredentialValidationResult.NOT_VALIDATED_RESULT;
		}
		return credentialValidationResult;
	}

	@Override
	public Set<ValidationType> validationTypes() {
		return EnumSet.of(ValidationType.VALIDATE);
	}

	@Override
	public int priority() {
		return 200;
	}
}
