package lk.jiat.smartcity360.security;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import lk.jiat.smartcity360.config.ErrorResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * Global authentication and role-based authorization filter for JAX-RS endpoints.
 */
@Provider
public class AuthFilter implements ContainerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Inject
    private SessionRegistry sessionRegistry;

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        if ("auth/login".equals(path)) {
            return;
        }

        String authHeader = requestContext.getHeaderString(AUTH_HEADER);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            abort(requestContext, Response.Status.UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length()).trim();
        Optional<SessionRegistry.UserSession> sessionOpt = sessionRegistry.getSession(token);
        if (sessionOpt.isEmpty()) {
            abort(requestContext, Response.Status.UNAUTHORIZED, "Session expired or invalid");
            return;
        }

        SessionRegistry.UserSession session = sessionOpt.get();
        requestContext.setProperty("userSession", session);

        if (!isAuthorized(session.getRole(), requestContext.getMethod(), path)) {
            abort(requestContext, Response.Status.FORBIDDEN, "Access denied for role " + session.getRole());
        }
    }

    private boolean isAuthorized(String role, String httpMethod, String path) {
        if ("ADMIN".equals(role)) {
            return true;
        }

        Method method = resourceInfo.getResourceMethod();
        RequiresRole annotation = method != null ? method.getAnnotation(RequiresRole.class) : null;
        if (annotation == null && resourceInfo.getResourceClass() != null) {
            annotation = resourceInfo.getResourceClass().getAnnotation(RequiresRole.class);
        }

        if (annotation != null) {
            return Arrays.asList(annotation.value()).contains(role);
        }

        if ("STAFF".equals(role)) {
            return true;
        }

        if ("RESIDENT".equals(role)) {
            return "GET".equalsIgnoreCase(httpMethod) || ("POST".equalsIgnoreCase(httpMethod) && path.startsWith("complaints"));
        }

        return false;
    }

    private void abort(ContainerRequestContext ctx, Response.Status status, String msg) {
        ctx.abortWith(Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponse(status.name(), msg))
                .build());
    }
}
