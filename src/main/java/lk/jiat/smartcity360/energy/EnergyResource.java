package lk.jiat.smartcity360.energy;

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
 * REST endpoint exposing energy metering operations and stream previewing.
 */
@Path("/energy")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EnergyResource {

    @Inject
    private EnergyService energyService;

    @GET
    @Path("/meters")
    public List<EnergyMeterDto> getMeters() {
        return energyService.getAllMeters();
    }

    @POST
    @Path("/meters")
    @RequiresRole({"ADMIN"})
    public Response createMeter(CreateMeterRequest request) {
        EnergyMeterDto created = energyService.createMeter(request);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/meters/{id}/layers")
    @RequiresRole({"ADMIN"})
    public EnergyMeterDto updateLayers(@PathParam("id") Long id, UpdateLayersRequest request) {
        return energyService.updateLayers(id, request);
    }

    @POST
    @Path("/meters/{id}/readings")
    public Response recordReading(@PathParam("id") Long id, SubmitReadingRequest request) {
        MeterReadingDto recorded = energyService.recordReading(id, request);
        return Response.status(Response.Status.CREATED).entity(recorded).build();
    }

    @GET
    @Path("/meters/{id}/readings")
    public List<MeterReadingDto> getReadings(@PathParam("id") Long id) {
        return energyService.getReadings(id);
    }

    @POST
    @Path("/preview")
    public PreviewReadingResponse preview(PreviewReadingRequest request) {
        return energyService.preview(request);
    }
}
