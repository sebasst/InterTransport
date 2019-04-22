package com.sagatechs.javaeeApp.rest.security;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.sagatechs.javaeeApp.dao.base.GenericDaoJpa;
import com.sagatechs.javaeeApp.model.base.Status;
import com.sagatechs.javaeeApp.rest.model.UserSecurity;

@Stateless
public class UserSecurityDao extends GenericDaoJpa<UserSecurity, Long> {

	public UserSecurityDao() {
		super(UserSecurity.class, Long.class);
	}

	public UserSecurity getByUsernameAndStatus(String username, Status status) {

		Query query = getEntityManager().createNamedQuery(UserSecurity.QUERY_FIND_BY_USERNAME_AND_STATUS,
				UserSecurity.class);
		query.setParameter(UserSecurity.QUERY_PARAM_USERNAME, username);
		query.setParameter(UserSecurity.QUERY_PARAM_STATUS, status);

		
		try {
			return (UserSecurity) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	public UserSecurity getByUsername(String username) {
		Query query = getEntityManager().createNamedQuery(UserSecurity.QUERY_FIND_BY_USERNAME, UserSecurity.class);
		query.setParameter(UserSecurity.QUERY_PARAM_USERNAME, username);
		try {
			return (UserSecurity) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

}
