package com.sagatechs.javaeeApp.rest.exceptionsMappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.logging.Logger;

import com.sagatechs.javaeeApp.exceptions.UnauthorizedException;
import com.sagatechs.javaeeApp.rest.model.ExceptionModelWs;

@Provider
public class UnauthorizatedMapper implements ExceptionMapper<UnauthorizedException> {

	private static final Logger LOGGER = Logger.getLogger(UnauthorizatedMapper.class);
	

	@Override
	public Response toResponse(UnauthorizedException exception) {
		LOGGER.debug(exception.getMessage());
		ExceptionModelWs exceptionModel= 
				new ExceptionModelWs(Response.Status.UNAUTHORIZED.getStatusCode(), exception.getMessage());
		return Response.status(Response.Status.UNAUTHORIZED).entity(exceptionModel).build();
	}

}