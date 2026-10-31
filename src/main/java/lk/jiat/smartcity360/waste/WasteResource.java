package lk.jiat.smartcity360.waste;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.jiat.smartcity360.security.RequiresRole;
import java.util.List;

/**
 * REST endpoint exposing municipal waste bin monitoring, fill updates, and collection triggers.
 */
@Path("/waste")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WasteResource {

    @Inject
    private WasteService wasteService;

    @GET
    @Path("/bins")
    public List<WasteBinDto> getBins() {
        return wasteService.getAll();
    }

    @POST
    @Path("/bins")
    @RequiresRole({"ADMIN"})
    public Response createBin(CreateWasteBinRequest request) {
        WasteBinDto created = wasteService.create(request);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/bins/{id}/fill")
    public WasteBinDto updateFill(@PathParam("id") Long id, UpdateFillRequest request) {
        return wasteService.updateFill(id, request);
    }

    @GET
    @Path("/bins/due")
    public List<WasteBinDto> getDueBins() {
        return wasteService.getDue();
    }

    @POST
    @Path("/bins/{id}/collect")
    public WasteBinDto collectBin(@PathParam("id") Long id) {
        return wasteService.collect(id);
    }
}
