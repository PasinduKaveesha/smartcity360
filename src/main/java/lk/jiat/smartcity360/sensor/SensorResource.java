package lk.jiat.smartcity360.sensor;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.jiat.smartcity360.security.RequiresRole;
import java.util.List;

/**
 * REST endpoint exposing IoT environmental sensor telemetry and readings intake.
 */
@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @Inject
    private SensorService sensorService;

    @GET
    public List<SensorDto> getSensors() {
        return sensorService.getAll();
    }

    @POST
    @RequiresRole({"ADMIN"})
    public Response createSensor(CreateSensorRequest request) {
        SensorDto created = sensorService.create(request);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @POST
    @Path("/{id}/readings")
    public Response recordReading(@PathParam("id") Long id, RecordSensorReadingRequest request) {
        SensorReadingDto recorded = sensorService.recordReading(id, request);
        return Response.status(Response.Status.CREATED).entity(recorded).build();
    }

    @GET
    @Path("/latest")
    public List<SensorReadingDto> getLatestReadings() {
        return sensorService.getLatestReadings();
    }
}
