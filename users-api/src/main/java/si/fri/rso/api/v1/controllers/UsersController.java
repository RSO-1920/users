package si.fri.rso.api.v1.controllers;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/users")
public class UsersController {

    @GET
    public Response getUsers() {
        return Response.status(Response.Status.OK).entity("ok").build();
    }
}
