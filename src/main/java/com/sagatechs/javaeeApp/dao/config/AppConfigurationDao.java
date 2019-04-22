package com.sagatechs.javaeeApp.dao.config;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import com.sagatechs.javaeeApp.dao.base.GenericDaoJpa;
import com.sagatechs.javaeeApp.model.config.AppConfiguration;
import com.sagatechs.javaeeApp.model.enums.AppConfigurationKey;

@LocalBean
@Stateless
public class AppConfigurationDao extends GenericDaoJpa<AppConfiguration, Long>{

	public AppConfigurationDao() {
		super(AppConfiguration.class, Long.class);
		
	}
	
	public AppConfiguration findByClave(AppConfigurationKey clave){
		
		try {
			return (AppConfiguration) entityManager.createNamedQuery(AppConfiguration.QUERY_FIND_BY_CLAVE).setParameter(AppConfiguration.QUERY_PARAM_CLAVE,clave).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
		
	}
	
	
	
	

}