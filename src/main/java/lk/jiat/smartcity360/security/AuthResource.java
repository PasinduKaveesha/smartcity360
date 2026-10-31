package lk.jiat.smartcity360.security;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lk.jiat.smartcity360.config.ErrorResponse;
import lk.jiat.smartcity360.persistence.dao.AppUserDao;
import lk.jiat.smartcity360.persistence.entity.AppUserEntity;
import java.util.Optional;

/**
 * REST resource handling authentication, logout, and identity inspection.
 */
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    private AppUserDao appUserDao;

    @Inject
    private SessionRegistry sessionRegistry;

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        if (request == null || request.getUsername() == null || request.getPassword() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("BAD_REQUEST", "Username and password required"))
                    .build();
        }

        Optional<AppUserEntity> userOpt = appUserDao.findByUsername(request.getUsername());
        if (userOpt.isEmpty() || !PasswordHasher.verify(request.getPassword(), userOpt.get().getPasswordHash())) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponse("UNAUTHORIZED", "Invalid username or password"))
                    .build();
        }

        AppUserEntity user = userOpt.get();
        String token = sessionRegistry.createSession(user.getId(), user.getUsername(), user.getRole(), user.getFullName());
        return Response.ok(new LoginResponse(token, user.getUsername(), user.getRole(), user.getFullName())).build();
    }

    @POST
    @Path("/logout")
    public Response logout(@HeaderParam("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7).trim();
            sessionRegistry.removeSession(token);
        }
        return Response.ok(new ErrorResponse("SUCCESS", "Logged out successfully")).build();
    }

    @GET
    @Path("/me")
    public Response me(@HeaderParam("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponse("UNAUTHORIZED", "Not logged in"))
                    .build();
        }
        String token = authHeader.substring(7).trim();
        Optional<SessionRegistry.UserSession> sessionOpt = sessionRegistry.getSession(token);
        if (sessionOpt.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponse("UNAUTHORIZED", "Session expired"))
                    .build();
        }

        SessionRegistry.UserSession s = sessionOpt.get();
        return Response.ok(new LoginResponse(token, s.getUsername(), s.getRole(), s.getFullName())).build();
    }
}
