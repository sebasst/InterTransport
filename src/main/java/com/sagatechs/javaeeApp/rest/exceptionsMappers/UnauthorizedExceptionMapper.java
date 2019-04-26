package com.sagatechs.javaeeApp.rest.exceptionsMappers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.sagatechs.javaeeApp.exceptions.UnauthorizedException;
import com.sagatechs.javaeeApp.rest.security.webModel.ExceptionModelWs;

@Provider
public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {

	@Override
	public Response toResponse(UnauthorizedException exception) {
		ExceptionModelWs errorResponse = new ExceptionModelWs(Status.UNAUTHORIZED.getStatusCode(),
				exception.getMessage());
		return Response.status(Status.UNAUTHORIZED).entity(errorResponse).type(MediaType.APPLICATION_JSON).build();

	}

}