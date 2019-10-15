package resources;

import javax.ws.rs.core.Response;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@ApplicationScoped
@Path("/users")
public class UsersResources {

    @GET
    public Response getUsers() {
        return Response.status(Response.Status.OK).entity("ok").build();
    }
}
