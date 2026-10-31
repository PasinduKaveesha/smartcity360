package lk.jiat.smartcity360.config;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Global JAX-RS exception mapper mapping exceptions to JSON error responses.
 */
@Provider
public class ApiExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(ApiExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        LOGGER.log(Level.WARNING, "API Exception caught: " + exception.getMessage(), exception);

        if (exception instanceof WebApplicationException webAppEx) {
            int status = webAppEx.getResponse().getStatus();
            String msg = webAppEx.getMessage() != null ? webAppEx.getMessage() : "Request error";
            return Response.status(status)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(new ErrorResponse("HTTP_" + status, msg))
                    .build();
        }

        if (isBadRequestException(exception)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(new ErrorResponse("BAD_REQUEST", exception.getMessage()))
                    .build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponse("INTERNAL_SERVER_ERROR", exception.getMessage()))
                .build();
    }

    private boolean isBadRequestException(Throwable ex) {
        String name = ex.getClass().getSimpleName();
        return ex instanceof IllegalArgumentException
                || ex instanceof IllegalStateException
                || name.contains("Syntax")
                || name.contains("Validation")
                || name.contains("Authentication");
    }
}
