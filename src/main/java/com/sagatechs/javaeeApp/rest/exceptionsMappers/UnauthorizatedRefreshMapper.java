package com.sagatechs.javaeeApp.rest.exceptionsMappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.logging.Logger;

import com.sagatechs.javaeeApp.exceptions.UnauthorizedRefreshException;
import com.sagatechs.javaeeApp.rest.model.ExceptionModelWs;

@Provider
public class UnauthorizatedRefreshMapper implements ExceptionMapper<UnauthorizedRefreshException> {

	private static final Logger LOGGER = Logger.getLogger(UnauthorizatedRefreshMapper.class);
	

	@Override
	public Response toResponse(UnauthorizedRefreshException exception) {
		LOGGER.debug(exception.getMessage());
		ExceptionModelWs exceptionModel= 
				new ExceptionModelWs(512, exception.getMessage());
		return Response.status(512).entity(exceptionModel).build();
	}

}