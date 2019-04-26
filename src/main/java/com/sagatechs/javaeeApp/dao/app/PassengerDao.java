package com.sagatechs.javaeeApp.dao.app;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.sagatechs.javaeeApp.dao.base.GenericDaoJpa;
import com.sagatechs.javaeeApp.model.app.Passenger;

@Stateless
public class PassengerDao extends GenericDaoJpa<Passenger, Long> {

	public PassengerDao() {
		super(Passenger.class, Long.class);
	}
	
	public Passenger getByUsername(String username) {
		Query q = getEntityManager().createNamedQuery(Passenger.QUERY_FIND_BY_USERNAME, Passenger.class);
		q.setParameter(Passenger.QUERY_PARAM_USERNAME, username);
		
		try {
			return (Passenger) q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public Passenger getByEmail(String email) {
		Query q = getEntityManager().createNamedQuery(Passenger.QUERY_FIND_BY_EMAIL, Passenger.class);
		q.setParameter(Passenger.QUERY_PARAM_EMAIL, email);
		
		try {
			return (Passenger) q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
