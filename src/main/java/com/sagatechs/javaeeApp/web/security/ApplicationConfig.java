package com.sagatechs.javaeeApp.web.security;
import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;

@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/login.xhtml",
                errorPage = "/login.xhtml",
                useForwardToLogin = false
            )
)
//@WebServlet(urlPatterns = {"/pages/protected/*"})
@ApplicationScoped
public class ApplicationConfig {
	
}