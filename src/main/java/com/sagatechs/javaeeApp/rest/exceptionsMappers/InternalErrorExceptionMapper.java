package com.sagatechs.javaeeApp.rest.exceptionsMappers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.sagatechs.javaeeApp.exceptions.InternalErrorException;
import com.sagatechs.javaeeApp.rest.security.webModel.ExceptionModelWs;

@Provider
public class InternalErrorExceptionMapper implements ExceptionMapper<InternalErrorException> {

	@Override
	public Response toResponse(InternalErrorException exception) {
		ExceptionModelWs errorResponse = new ExceptionModelWs(Status.INTERNAL_SERVER_ERROR.getStatusCode(),
				exception.getMessage());
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorResponse).type(MediaType.APPLICATION_JSON)
				.build();
	}

}