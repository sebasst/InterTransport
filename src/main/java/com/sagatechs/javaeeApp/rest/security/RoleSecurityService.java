package com.sagatechs.javaeeApp.rest.security;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.sagatechs.javaeeApp.rest.security.model.Role;
import com.sagatechs.javaeeApp.rest.security.model.RoleSecurity;

@Stateless
public class RoleSecurityService {

	@Inject
	RoleSecurityDao roleSecurityDao;
	
	public RoleSecurity getByName(Role name) {
		return this.roleSecurityDao.getByName(name);
	}
	
}
