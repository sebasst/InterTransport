package com.sagatechs.javaeeApp.services.app;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.sagatechs.javaeeApp.dao.app.PassengerDao;
import com.sagatechs.javaeeApp.exceptions.GeneralAppException;
import com.sagatechs.javaeeApp.exceptions.NotFoundException;
import com.sagatechs.javaeeApp.model.app.Passenger;
import com.sagatechs.javaeeApp.model.base.Status;
import com.sagatechs.javaeeApp.rest.security.RoleSecurityService;
import com.sagatechs.javaeeApp.rest.security.UserSecurityService;
import com.sagatechs.javaeeApp.rest.security.model.Role;
import com.sagatechs.javaeeApp.rest.security.model.RoleSecurity;
import com.sagatechs.javaeeApp.rest.security.model.UserSecurity;
import com.sagatechs.javaeeApp.rest.security.webModel.PassengerWebModel;
import com.sagatechs.javaeeApp.utils.StringUtils;

@Stateless
public class PassengerService {

	private static final Logger LOGGER = Logger.getLogger(Passenger.class);

	@Inject
	PassengerDao passengerDao;

	@Inject
	RoleSecurityService roleSecurityService;

	@Inject
	UserSecurityService userSecurityService;

	@Inject
	StringUtils stringUtils;

	public Passenger createPassengerFromPassengerWeb(PassengerWebModel passengerWeb) {
		Passenger passenger = new Passenger();
		passenger.setFirstName(passengerWeb.getFirstName());
		passenger.setLastName(passengerWeb.getLastName());
		passenger.setIdDocument(passengerWeb.getIdDocument());

		UserSecurity user = new UserSecurity();
		user.setEmail(passengerWeb.getEmail());
		user.setPhoneNumber(passengerWeb.getPhoneNumber());
		user.setUsername(passengerWeb.getEmail());
		user.setStatus(Status.ACTIVE);

		byte[] passwordEnc;

		passwordEnc = this.stringUtils.encryptPassword(passengerWeb.getPassword());

		user.setPassword(passwordEnc);
		// TODO QUITAR REDUNDANCIA DE RELACIONES
		RoleSecurity rolePasajero = this.roleSecurityService.getByName(Role.PASAJERO);
		user.getRoles().add(rolePasajero);
		rolePasajero.getUsers().add(user);

		passenger.setUser(user);

		this.userSecurityService.createUser(user);
		this.passengerDao.persist(passenger);
		return passenger;
	}

	public Passenger updatePassengerFromPassengerWeb(PassengerWebModel passengerWeb) throws GeneralAppException {

		Passenger passenger = this.passengerDao.find(passengerWeb.getId());
		if (passenger == null) {
			throw new NotFoundException("Pasajero no encontrado.");
		}
		passenger.setFirstName(passengerWeb.getFirstName());
		passenger.setLastName(passengerWeb.getLastName());
		passenger.setIdDocument(passengerWeb.getIdDocument());

		UserSecurity user = passenger.getUser();
		if (user == null) {
			// se frego , inconsistencia
			String erromsg = "No existe usuario para el pasajero con id " + passenger.getId() + " ";
			LOGGER.error(erromsg);
			throw new GeneralAppException(erromsg);
		}

		user.setEmail(passengerWeb.getEmail());
		user.setUsername(passengerWeb.getEmail());
		user.setPhoneNumber(passengerWeb.getPhoneNumber());
		

		this.userSecurityService.updateUser(user);
		this.passengerDao.update(passenger);
		return passenger;
	}

	public PassengerWebModel getPassengertoPassengerWeb(Long id) throws GeneralAppException {

		Passenger passenger = this.passengerDao.find(id);
		if (passenger == null) {
			throw new NotFoundException("Pasajero no encontrado.");
		}

		UserSecurity user = passenger.getUser();
		if (user == null) {
			// se frego , inconsistencia
			String erromsg = "No existe usuario para el pasajero con id " + passenger.getId() + " ";
			LOGGER.error(erromsg);
			throw new GeneralAppException(erromsg);
		}

		PassengerWebModel pwm = new PassengerWebModel();
		pwm.setEmail(user.getEmail());
		pwm.setFirstName(passenger.getFirstName());
		pwm.setId(passenger.getId());
		pwm.setIdDocument(passenger.getIdDocument());
		pwm.setLastName(passenger.getLastName());
		pwm.setPhoneNumber(user.getPhoneNumber());
		return pwm;
	}

	public PassengerWebModel getPassengertoPassengerWebByEmail(String email) throws GeneralAppException {

		Passenger passenger = this.passengerDao.getByEmail(email);
		if (passenger == null) {
			throw new NotFoundException("Pasajero no encontrado.");
		}

		return this.passengerToPassengerWebModel(passenger);
	}
	
	public PassengerWebModel getPassengertoPassengerWebByUsername(String username) throws GeneralAppException {

		Passenger passenger = this.passengerDao.getByUsername(username);
		if (passenger == null) {
			throw new NotFoundException("Pasajero no encontrado.");
		}

		return this.passengerToPassengerWebModel(passenger);
	}

	private PassengerWebModel passengerToPassengerWebModel(Passenger passenger) throws GeneralAppException {

		UserSecurity user = passenger.getUser();
		if (user == null) {
			// se frego , inconsistencia
			String erromsg = "No existe usuario para el pasajero con id " + passenger.getId() + " ";
			LOGGER.error(erromsg);
			throw new GeneralAppException(erromsg);
		}

		PassengerWebModel pwm = new PassengerWebModel();
		pwm.setEmail(user.getEmail());
		pwm.setFirstName(passenger.getFirstName());
		pwm.setId(passenger.getId());
		pwm.setIdDocument(passenger.getIdDocument());
		pwm.setLastName(passenger.getLastName());
		pwm.setPhoneNumber(user.getPhoneNumber());
		return pwm;

	}
}
