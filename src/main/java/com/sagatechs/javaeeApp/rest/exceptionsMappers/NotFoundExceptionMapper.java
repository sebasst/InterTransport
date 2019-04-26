package com.sagatechs.javaeeApp.rest.exceptionsMappers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.sagatechs.javaeeApp.exceptions.NotFoundException;
import com.sagatechs.javaeeApp.rest.security.webModel.ExceptionModelWs;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

	@Override
	public Response toResponse(NotFoundException exception) {
		ExceptionModelWs errorResponse = new ExceptionModelWs(Status.NOT_FOUND.getStatusCode(),
				exception.getMessage());
		return Response.status(Status.NOT_FOUND).entity(errorResponse).type(MediaType.APPLICATION_JSON).build();

	}

}