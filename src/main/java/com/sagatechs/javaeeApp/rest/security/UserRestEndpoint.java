package com.sagatechs.javaeeApp.rest.security;

import java.security.Principal;

import javax.annotation.security.RolesAllowed;
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
import com.sagatechs.javaeeApp.rest.security.webModel.ChangePasswordRequest;
import com.sagatechs.javaeeApp.rest.security.webModel.CodeEmailVerificarionRequest;
import com.sagatechs.javaeeApp.rest.security.webModel.CodeEmailVerificarionResponse;
import com.sagatechs.javaeeApp.rest.security.webModel.Credentials;
import com.sagatechs.javaeeApp.rest.security.webModel.EmailVerificarionResponse;
import com.sagatechs.javaeeApp.rest.security.webModel.EmailVerificationRequest;
import com.sagatechs.javaeeApp.rest.security.webModel.RefreshToken;
import com.sagatechs.javaeeApp.rest.security.webModel.Tokens;

@Path("/authentication")
@RequestScoped
public class UserRestEndpoint {

	@Inject
	UserSecurityService userSecurityService;

	

	/**
	 * Autentica usuarios
	 * @param credentials
	 * @return
	 * @throws UnauthorizedException
	 */
	@Path("/login")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Tokens authenticateUser(Credentials credentials) throws UnauthorizedException {
		String username = credentials.getUsername();
		String password = credentials.getPassword();
		return userSecurityService.authenticateRest(username, password);
	}

	/**
	 * Regresca nuevo token
	 * @param refreshToken
	 * @return
	 * @throws UnauthorizedException
	 * @throws UnauthorizedRefreshException
	 */
	@Path("/refresh")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Tokens refreshToken(RefreshToken refreshToken) throws UnauthorizedException {

		return userSecurityService.refreshToken(refreshToken.getRefresh_token());
	}

	/**
	 * Quita tokens para que se deba autenticar nuevamente
	 * @param securityContext
	 * @throws UnauthorizedException
	 */
	@Path("/logout")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Secured
	@RolesAllowed("PASAJERO")
	public void logout(@Context SecurityContext securityContext) throws UnauthorizedException {
		Principal principal = securityContext.getUserPrincipal();
		String username = principal.getName();
		userSecurityService.logout(username);
	}

	/**
	 * Verifica si ya existe el usuario, si no existe envia un codigo para q se registre
	 * @param emailRequest
	 * @return
	 * @throws UnauthorizedException
	 */
	@Path("/emailRegistrationVerification")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public EmailVerificarionResponse verifyUserEmailRegistration(EmailVerificationRequest emailRequest)
			throws UnauthorizedException {
		EmailVerificarionResponse reponse = new EmailVerificarionResponse();

		boolean result = userSecurityService.verifyEmailRegistration(emailRequest.getEmail());
		reponse.setEmailIsRegistered(result);
		return reponse;
	}

	/**
	 * Verifica el codigo paara el registro
	 * @param codeEmailVerification
	 * @return
	 */
	@Path("/codeEmailVerification")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public CodeEmailVerificarionResponse verifyCodeUserEmail(CodeEmailVerificarionRequest codeEmailVerification) {
		CodeEmailVerificarionResponse response = new CodeEmailVerificarionResponse();
		boolean result = this.userSecurityService.codeEmailVerification(codeEmailVerification.getEmail(),
				codeEmailVerification.getCode());
		response.setCodeIsCorrect(result);
		return response;
	}
	

	/**
	 * Cambia la contraseña
	 * @param changePasswordRequest
	 * @return
	 */
	@Path("/changePasswordSendCode")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public EmailVerificarionResponse changePasswordSendCode(EmailVerificationRequest emailVerificationRequest) {
		EmailVerificarionResponse response = new EmailVerificarionResponse();
		boolean result =this.userSecurityService.changePasswordSendCode(emailVerificationRequest.getEmail());
		response.setEmailIsRegistered(result);
		return response;
	}
	
	/**
	 * Cambia la contraseña
	 * @param changePasswordRequest
	 * @return
	 */
	@Path("/changePassword")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public CodeEmailVerificarionResponse changePassword(ChangePasswordRequest changePasswordRequest) {
		CodeEmailVerificarionResponse response = new CodeEmailVerificarionResponse();
		boolean result =this.userSecurityService.changePasswordFromWsRequest(changePasswordRequest);
		response.setCodeIsCorrect(result);
		return response;
	}
	

}