package com.sagatechs.javaeeApp.web.security.rest;
import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

import java.util.Arrays;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 *
 * @author Sukma Wardana
 * @since 1.0
 */
@Stateless
@Path("tokens")
public class AuthResource {

    @Inject
    JWTStore jwtStore;

    /**
     *
     * @param credential in json should be {"username": "...", "password": "..."}
     * @return JWT token
     */
    @POST
    public Response authenticate(JsonObject credential) {
        // TODO: Should compare user credentials on the database.
        String username = credential.getString("username");
        String password = credential.getString("password");

        // TODO: Groups should retrieve from database based on authenticate user.
        String token = this.jwtStore.generateToken(username, password);

        return Response.ok().header(AUTHORIZATION, "Bearer " + token).build();
    }
}