package lk.jiat.smartcity360.complaint;

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
 * REST endpoint exposing complaint submission, audit trace inspection, and escalation chain management.
 */
@Path("/complaints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ComplaintResource {

    @Inject
    private ComplaintService complaintService;

    @POST
    public Response submitComplaint(CreateComplaintRequest request) {
        ComplaintDto created = complaintService.submitComplaint(request);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    public List<ComplaintDto> getComplaints() {
        return complaintService.getAllComplaints();
    }

    @GET
    @Path("/{id}")
    public ComplaintDto getComplaintById(@PathParam("id") Long id) {
        return complaintService.getComplaintById(id);
    }

    @GET
    @Path("/chain")
    public EscalationChainDto getChain() {
        return complaintService.getChainOrder();
    }

    @PUT
    @Path("/chain")
    @RequiresRole({"ADMIN"})
    public EscalationChainDto updateChain(UpdateChainRequest request) {
        return complaintService.updateChainOrder(request);
    }
}
