package com.sagatechs.javaeeApp.rest.security;

import java.security.Principal;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import com.sagatechs.javaeeApp.exceptions.UnauthorizedException;
import com.sagatechs.javaeeApp.exceptions.UnauthorizedRefreshException;
import com.sagatechs.javaeeApp.rest.model.Credentials;
import com.sagatechs.javaeeApp.rest.model.RefreshToken;
import com.sagatechs.javaeeApp.rest.model.Tokens;

@Path("/authentication")
@RequestScoped
public class UserRestEndpoint {
	
	@Inject
	UserSecurityService  userSecurityService;
	
	@Path("/login")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Tokens authenticateUser(Credentials credentials) throws UnauthorizedException  {
		String username = credentials.getUsername();
		String password = credentials.getPassword();
		return userSecurityService.authenticateRest(username, password);
	}
	
	@Path("/refresh")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Tokens refreshToken(RefreshToken refreshToken) throws UnauthorizedException, UnauthorizedRefreshException  {
		
		return userSecurityService.refreshToken(refreshToken.getRefresh_token());
	}
	
	@Path("/logout")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Secured
	public void logout(@Context SecurityContext securityContext) throws UnauthorizedException  {
		Principal principal = securityContext.getUserPrincipal();
		String username = principal.getName();
		userSecurityService.logout(username);
	}

	
}