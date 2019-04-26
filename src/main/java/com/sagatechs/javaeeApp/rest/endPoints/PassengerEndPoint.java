package com.sagatechs.javaeeApp.rest.endPoints;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sagatechs.javaeeApp.exceptions.GeneralAppException;
import com.sagatechs.javaeeApp.model.app.Passenger;
import com.sagatechs.javaeeApp.rest.security.Secured;
import com.sagatechs.javaeeApp.rest.security.webModel.PassengerWebModel;
import com.sagatechs.javaeeApp.rest.webModel.IdWebModel;
import com.sagatechs.javaeeApp.services.app.PassengerService;

@Path("/passenger")
@RequestScoped
public class PassengerEndPoint {
	@Inject
	PassengerService passengerService;

	@Path("/")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public IdWebModel createPassenger(PassengerWebModel passengerWeb) {
		Passenger result = this.passengerService.createPassengerFromPassengerWeb(passengerWeb);
		IdWebModel idWeb = new IdWebModel(result.getId());
		return idWeb;
	}

	@Path("/{id}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@PermitAll
	@Secured
	public IdWebModel updatePassenger(@PathParam("id") Long id, PassengerWebModel passengerWeb) throws GeneralAppException {
		passengerWeb.setId(id);
		Passenger result = this.passengerService.updatePassengerFromPassengerWeb(passengerWeb);
		IdWebModel idWeb = new IdWebModel(result.getId());
		return idWeb;
	}
	
	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@PermitAll
	@Secured
	public PassengerWebModel getPassenger(@PathParam("id") Long id) throws GeneralAppException {
		
		return  this.passengerService.getPassengertoPassengerWeb(id);
		
	}
	
	@Path("/email/{email}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@PermitAll
	@Secured
	public PassengerWebModel getPassengerByEmail(@PathParam("email") String email) throws GeneralAppException {
		
		return  this.passengerService.getPassengertoPassengerWebByEmail(email);
		
	}
	
	@Path("/username/{username}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@PermitAll
	@Secured
	public PassengerWebModel getPassengerByUsername(@PathParam("username") String username) throws GeneralAppException {
		
		return  this.passengerService.getPassengertoPassengerWebByUsername(username);
		
	}

}
