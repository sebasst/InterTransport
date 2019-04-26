package com.sagatechs.javaeeApp.rest.exceptionsMappers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.sagatechs.javaeeApp.exceptions.AccessDeniedException;
import com.sagatechs.javaeeApp.rest.security.webModel.ExceptionModelWs;

@Provider
public class AccessDeniedExceptionMapper implements ExceptionMapper<AccessDeniedException> {

	@Override
	public Response toResponse(AccessDeniedException exception) {
		ExceptionModelWs errorResponse = new ExceptionModelWs(Status.FORBIDDEN.getStatusCode(),
				exception.getMessage());
		return Response.status(Status.UNAUTHORIZED).entity(errorResponse).type(MediaType.APPLICATION_JSON).build();

	}

}