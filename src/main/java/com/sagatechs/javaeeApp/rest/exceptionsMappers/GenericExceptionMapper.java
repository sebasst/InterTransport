package com.sagatechs.javaeeApp.rest.exceptionsMappers;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.sagatechs.javaeeApp.rest.security.webModel.ExceptionModelWs;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable ex) {

		ExceptionModelWs errorResponse = new ExceptionModelWs(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ExceptionUtils.getRootCauseMessage(ex));
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorResponse).type(MediaType.APPLICATION_JSON)
				.build();
	}
}