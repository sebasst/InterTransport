package com.sagatechs.javaeeApp.web.security;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

import com.sagatechs.javaeeApp.rest.security.UserSecurityService;

@ApplicationScoped
public class RolesIdentityStore implements IdentityStore {

	@Inject
	UserSecurityService userSecurityService;

	

	@Override
	public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
		
		 List<String> rolesList = this.userSecurityService.getRolesByUsernameFromRepository(validationResult.getCallerPrincipal().getName());
		return new HashSet<>(rolesList);
	}



	@Override
	public Set<ValidationType> validationTypes() {
		return EnumSet.of(ValidationType.PROVIDE_GROUPS);
	}

}
