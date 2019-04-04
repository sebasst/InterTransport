package com.sagatechs.javaeeApp.web.security;

import static com.sagatechs.adminfaces.starter.util.Utils.addDetailMessage;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

@Named
@RequestScoped
public class LoginController {

	@Inject
	private SecurityContext securityContext;

	@Inject
	private User user;
	
	private boolean remember;

	public void login() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletRequest httpServletRequest = (HttpServletRequest) externalContext.getRequest();
		HttpServletResponse httpServletResponse = (HttpServletResponse) externalContext.getResponse();
		UsernamePasswordCredential usernamePasswordCredential = new UsernamePasswordCredential(user.getUserName(),
				user.getPassword());

		AuthenticationParameters authenticationParameters = AuthenticationParameters.withParams()
				.credential(usernamePasswordCredential);

		AuthenticationStatus authenticationStatus = securityContext.authenticate(httpServletRequest,
				httpServletResponse, authenticationParameters);

		switch (authenticationStatus) {
		case SEND_CONTINUE:
			facesContext.responseComplete();
			break;
		case SEND_FAILURE:
			Messages.addError(null, "Usuario o contraseña incorrectos");
			externalContext.getFlash().setKeepMessages(true);
			break;
		case SUCCESS:
			externalContext.getFlash().setKeepMessages(true);
			addDetailMessage("Bienvenido <b>" + user.getUserName() + "</b>");
			Faces.redirect("index.xhtml");
			break;
		case NOT_DONE:
			Messages.addError(null, "Login failed");
		}

	}
	
	public boolean isLoggedIn() {
		return securityContext.getCallerPrincipal() != null;
	}
	
	public String getCurrentUser() {
		return securityContext.getCallerPrincipal() != null ? securityContext.getCallerPrincipal().getName() : "";
	}

	public boolean isRemember() {
		return remember;
	}

	public void setRemember(boolean remember) {
		this.remember = remember;
	}
	
	
}