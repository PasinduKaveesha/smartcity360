package lk.jiat.smartcity360.resident;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * REST endpoint exposing resident registration and municipal staff directory.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ResidentResource {

    @Inject
    private ResidentService residentService;

    @GET
    @Path("/residents")
    public List<ResidentDto> getResidents() {
        return residentService.getAllResidents();
    }

    @POST
    @Path("/residents")
    public Response createResident(CreateResidentRequest request) {
        ResidentDto created = residentService.createResident(request);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    @Path("/staff")
    public List<StaffMemberDto> getStaff() {
        return residentService.getAllStaff();
    }
}
