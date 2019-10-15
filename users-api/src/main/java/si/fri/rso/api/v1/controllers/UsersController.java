package si.fri.rso.api.v1.controllers;

import si.fri.rso.services.UsersBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersController {

    @Inject
    private UsersBean usersBean;

    @GET
    public Response getUsers() {
        usersBean.getAllUsers();
        return Response.status(Response.Status.OK).entity("ok").build();
    }
}
