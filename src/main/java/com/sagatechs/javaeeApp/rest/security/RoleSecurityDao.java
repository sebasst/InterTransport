package com.sagatechs.javaeeApp.rest.security;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.sagatechs.javaeeApp.dao.base.GenericDaoJpa;
import com.sagatechs.javaeeApp.model.base.Status;
import com.sagatechs.javaeeApp.rest.model.RoleSecurity;
import com.sagatechs.javaeeApp.rest.model.UserSecurity;

@Stateless
public class RoleSecurityDao extends GenericDaoJpa<RoleSecurity, Long> {

	public RoleSecurityDao() {
		super(RoleSecurity.class, Long.class);
	}

	@SuppressWarnings("unchecked")
	public List<RoleSecurity> getByUsername(String username, Status status) {
		Query query = getEntityManager().createNamedQuery(RoleSecurity.QUERY_FIND_BY_USERNAME_AND_STATUS,
				RoleSecurity.class);
		query.setParameter(RoleSecurity.QUERY_PARAM_USERNAME, username);
		query.setParameter(RoleSecurity.QUERY_PARAM_STATUS, status);

		return  query.getResultList();

	}
	
	public RoleSecurity getByName(Role name) {
		Query query = getEntityManager().createNamedQuery(RoleSecurity.QUERY_FIND_BY_NAME,
				RoleSecurity.class);
		query.setParameter(RoleSecurity.QUERY_PARAM_NAME, name);

		try {
			return  (RoleSecurity) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

}
