package lk.jiat.smartcity360.zone;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.jiat.smartcity360.config.ErrorResponse;
import lk.jiat.smartcity360.security.RequiresRole;
import java.util.List;

/**
 * REST endpoint exposing zone hierarchy, power controls, and usage reports.
 */
@Path("/zones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ZoneResource {

    @Inject
    private ZoneService zoneService;

    @GET
    @Path("/tree")
    public List<ZoneNodeDto> getTree() {
        return zoneService.getTree();
    }

    @POST
    @RequiresRole({"ADMIN"})
    public Response createZone(CreateZoneRequest request) {
        ZoneNodeDto created = zoneService.createZone(request);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @POST
    @Path("/{id}/shutdown")
    @RequiresRole({"ADMIN"})
    public Response shutdownZone(@PathParam("id") Long id) {
        zoneService.shutdownZone(id);
        return Response.ok(new ErrorResponse("SUCCESS", "Power shutdown propagated across zone " + id)).build();
    }

    @POST
    @Path("/{id}/restore")
    @RequiresRole({"ADMIN"})
    public Response restoreZone(@PathParam("id") Long id) {
        zoneService.restoreZone(id);
        return Response.ok(new ErrorResponse("SUCCESS", "Power restored across zone " + id)).build();
    }

    @GET
    @Path("/{id}/report")
    public ZoneReportDto getReport(@PathParam("id") Long id) {
        return zoneService.getReport(id);
    }
}
