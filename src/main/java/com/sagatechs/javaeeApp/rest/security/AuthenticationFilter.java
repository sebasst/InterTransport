package com.sagatechs.javaeeApp.rest.security;

import java.io.IOException;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.sagatechs.javaeeApp.exceptions.UnauthorizedException;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

	private static final String REALM = "example";
	private static final String AUTHENTICATION_SCHEME = "Bearer";

	@Inject
	private SecurityContext securityContext;

	@Inject
	UserSecurityService userSecurityService;

	@Context
	private HttpServletRequest httpRequest;
	
	@Context
	private HttpServletResponse httpResponse;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		// Get the Authorization header from the request
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

		// Validate the Authorization header
		if (!isTokenBasedAuthentication(authorizationHeader)) {
			abortWithUnauthorized(requestContext);
			return;
		}

		// Extract the token from the Authorization header
		String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();

		try {

			// Validate the token
			//String username = validateToken(token);
			UsernameJwtCredential credential = new UsernameJwtCredential(token);
			AuthenticationParameters authenticationParameters = AuthenticationParameters.withParams()
					.credential(credential);
			AuthenticationStatus authenticationStatus = securityContext.authenticate(httpRequest, httpResponse, authenticationParameters);
			
			switch (authenticationStatus) {
			case SUCCESS:
				return;

			default:
				abortWithUnauthorized(requestContext);
			}

			// AuthenticationStatus authenticationStatus =
			// securityContext.authenticate(requestContext., response, parameters)

			// set security context

			/*
			 * 
			 * final SecurityContext currentSecurityContext =
			 * requestContext.getSecurityContext(); requestContext.setSecurityContext(new
			 * SecurityContext() {
			 * 
			 * @Override public Principal getUserPrincipal() { return () -> username; }
			 * 
			 * @Override public boolean isUserInRole(String role) { return true; }
			 * 
			 * @Override public boolean isSecure() { return
			 * currentSecurityContext.isSecure(); }
			 * 
			 * @Override public String getAuthenticationScheme() { return
			 * AUTHENTICATION_SCHEME; } });
			 */

		} catch (Exception e) {
			e.printStackTrace();
			abortWithUnauthorized(requestContext);
		}
	}

	private boolean isTokenBasedAuthentication(String authorizationHeader) {

		// Check if the Authorization header is valid
		// It must not be null and must be prefixed with "Bearer" plus a whitespace
		// The authentication scheme comparison must be case-insensitive
		return authorizationHeader != null
				&& authorizationHeader.toLowerCase().startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
	}

	private void abortWithUnauthorized(ContainerRequestContext requestContext) {

		// Abort the filter chain with a 401 status code response
		// The WWW-Authenticate header is sent along with the response
		requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
				.header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEME + " realm=\"" + REALM + "\"").build());
	}

	private String validateToken(String token)  {
		return userSecurityService.validateToken(token);
	}
}