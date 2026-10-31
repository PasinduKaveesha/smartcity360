package lk.jiat.smartcity360.streetlight;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.jiat.smartcity360.config.ErrorResponse;
import lk.jiat.smartcity360.security.RequiresRole;
import java.util.List;

/**
 * REST endpoint exposing streetlight monitoring, state controls, batch creation, and flyweight stats.
 */
@Path("/streetlights")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StreetlightResource {

    @Inject
    private StreetlightService streetlightService;

    @GET
    public List<StreetlightDto> getStreetlights(
            @QueryParam("zoneId") Long zoneId,
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("20") int size) {
        return streetlightService.getPaged(zoneId, page, size);
    }

    @GET
    @Path("/stats")
    public StreetlightStatsDto getStats() {
        return streetlightService.getStats();
    }

    @POST
    @RequiresRole({"ADMIN"})
    public Response createStreetlight(CreateStreetlightRequest request) {
        StreetlightDto created = streetlightService.create(request);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @POST
    @Path("/bulk")
    @RequiresRole({"ADMIN"})
    public Response bulkGenerate(
            @QueryParam("count") @DefaultValue("100") int count,
            @QueryParam("zoneId") Long zoneId) {
        int inserted = streetlightService.bulkGenerate(count, zoneId);
        return Response.status(Response.Status.CREATED)
                .entity(new ErrorResponse("SUCCESS", "Successfully bulk-generated " + inserted + " streetlights"))
                .build();
    }

    @PUT
    @Path("/{id}/state")
    public StreetlightDto updateState(@PathParam("id") Long id, UpdateLampStateRequest request) {
        return streetlightService.updateState(id, request);
    }
}
