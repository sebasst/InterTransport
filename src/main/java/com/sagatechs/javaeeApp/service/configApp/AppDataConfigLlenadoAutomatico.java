package com.sagatechs.javaeeApp.service.configApp;

import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.sagatechs.javaeeApp.model.base.Status;
import com.sagatechs.javaeeApp.rest.security.RoleSecurityDao;
import com.sagatechs.javaeeApp.rest.security.UserSecurityDao;
import com.sagatechs.javaeeApp.rest.security.model.Role;
import com.sagatechs.javaeeApp.rest.security.model.RoleSecurity;
import com.sagatechs.javaeeApp.rest.security.model.UserSecurity;
import com.sagatechs.javaeeApp.utils.StringUtils;

@Singleton
@Startup
public class AppDataConfigLlenadoAutomatico {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(AppDataConfigLlenadoAutomatico.class);

	@Inject
	UserSecurityDao userSecurityDao;

	@Inject
	RoleSecurityDao roleSecurityDao;

	@Inject
	StringUtils stringUtils;

	@PostConstruct
	private void init() {
		// createUsers();
		createUsers();

	}

	private void createUsers() {
		// role
		RoleSecurity rolePasajero = this.instantiateRoleSecurityValues(Role.PASAJERO, "Pasajero, usuario final");
		RoleSecurity roleAdministrador = this.instantiateRoleSecurityValues(Role.ADMINISTRADOR,
				"Administrado de la aplicación");
		RoleSecurity roleConductor = this.instantiateRoleSecurityValues(Role.CONDUCTOR, "Conductor ");
		RoleSecurity roleVisualizador = this.instantiateRoleSecurityValues(Role.VISUALIZADOR, "Visualizador");
		RoleSecurity roleGestor = this.instantiateRoleSecurityValues(Role.GESTOR, "Gestor de carreras");

		UserSecurity userpasajero = this.instantiateUserSecurityValues("userpasajero", "userpasajero");
		UserSecurity userconductor = this.instantiateUserSecurityValues("userconductor", "userconductor");
		UserSecurity uservisualizador = this.instantiateUserSecurityValues("uservisualizador", "uservisualizador");
		UserSecurity useradministrador = this.instantiateUserSecurityValues("useradministrador", "useradministrador");
		UserSecurity usergestor = this.instantiateUserSecurityValues("usergestor", "usergestor");
		UserSecurity usertotal = this.instantiateUserSecurityValues("usertotal", "usertotal");

		userpasajero.getRoles().add(rolePasajero);
		rolePasajero.getUsers().add(userpasajero);

		userconductor.getRoles().add(roleConductor);
		roleConductor.getUsers().add(userconductor);

		uservisualizador.getRoles().add(roleVisualizador);
		roleVisualizador.getUsers().add(uservisualizador);

		useradministrador.getRoles().add(roleAdministrador);
		roleAdministrador.getUsers().add(useradministrador);

		usergestor.getRoles().add(roleGestor);
		roleGestor.getUsers().add(usergestor);

		usertotal.getRoles().add(roleGestor);
		usertotal.getRoles().add(roleAdministrador);
		usertotal.getRoles().add(roleConductor);
		usertotal.getRoles().add(rolePasajero);
		usertotal.getRoles().add(roleVisualizador);

		roleGestor.getUsers().add(usertotal);
		roleAdministrador.getUsers().add(usertotal);
		roleConductor.getUsers().add(usertotal);
		rolePasajero.getUsers().add(usertotal);
		roleVisualizador.getUsers().add(usertotal);

		this.roleSecurityDao.update(roleAdministrador);
		this.roleSecurityDao.update(roleConductor);
		this.roleSecurityDao.update(roleGestor);
		this.roleSecurityDao.update(rolePasajero);
		this.roleSecurityDao.update(roleVisualizador);

		this.userSecurityDao.update(useradministrador);
		this.userSecurityDao.update(userconductor);
		this.userSecurityDao.update(usergestor);
		this.userSecurityDao.update(userpasajero);
		this.userSecurityDao.update(usertotal);
	}

	private UserSecurity instantiateUserSecurityValues(String username, String password) {
		UserSecurity user = this.userSecurityDao.getByUsername(username);
		if (user == null) {
			byte[] passwordEncr;

			passwordEncr = stringUtils.encryptPassword(password);

			user = new UserSecurity(username, passwordEncr, Status.ACTIVE);
			this.userSecurityDao.persist(user);
		}
		return user;

	}

	private RoleSecurity instantiateRoleSecurityValues(Role name, String description) {
		RoleSecurity role = this.roleSecurityDao.getByName(name);
		if (role == null) {

			role = new RoleSecurity(Status.ACTIVE, name, description);
			this.roleSecurityDao.persist(role);
		}
		return role;

	}

}