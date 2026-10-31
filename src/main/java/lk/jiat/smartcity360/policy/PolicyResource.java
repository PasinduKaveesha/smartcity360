package lk.jiat.smartcity360.policy;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
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
 * REST endpoint exposing policy rule administration, real-time syntax checking, and evaluation runs.
 */
@Path("/policies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PolicyResource {

    @Inject
    private PolicyService policyService;

    @GET
    public List<PolicyDto> getPolicies() {
        return policyService.getAll();
    }

    @POST
    @RequiresRole({"ADMIN"})
    public Response createPolicy(CreatePolicyRequest request) {
        PolicyDto created = policyService.create(request);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @RequiresRole({"ADMIN"})
    public PolicyDto updatePolicy(@PathParam("id") Long id, UpdatePolicyRequest request) {
        return policyService.update(id, request);
    }

    @DELETE
    @Path("/{id}")
    @RequiresRole({"ADMIN"})
    public Response deletePolicy(@PathParam("id") Long id) {
        policyService.delete(id);
        return Response.ok(new ErrorResponse("SUCCESS", "Policy deleted successfully")).build();
    }

    @POST
    @Path("/validate")
    public ValidationResultDto validatePolicy(ValidatePolicyRequest request) {
        return policyService.validate(request);
    }

    @POST
    @Path("/evaluate-all")
    public EvaluateAllResultDto evaluateAll() {
        return policyService.evaluateAll();
    }

    @GET
    @Path("/logs")
    public List<PolicyLogDto> getLogs(@QueryParam("limit") @DefaultValue("50") int limit) {
        return policyService.getRecentLogs(limit);
    }
}
