package lk.jiat.smartcity360.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * JAX-RS Application root exposing all REST endpoints under /api.
 */
@ApplicationPath("/api")
public class RestApplication extends Application {
}
