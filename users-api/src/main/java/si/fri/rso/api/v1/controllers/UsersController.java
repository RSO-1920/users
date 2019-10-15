package si.fri.rso.api.v1.controllers;

import si.fri.rso.lib.UserDTO;
import si.fri.rso.lib.UserModel;
import si.fri.rso.services.UsersBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersController {

    @Inject
    private UsersBean usersBean;

    @GET
    public Response getUsers() {
        List<UserModel> users = usersBean.getAllUsers();
        return Response.status(Response.Status.OK).entity(users).build();
    }

    @GET
    @Path("{userId}")
    public Response getUser(@PathParam("userId") Integer userId) {
        UserModel user = usersBean.getUser(userId);
        return Response.status(Response.Status.OK).entity(user).build();
    }

    @POST
    @Path("register")
    public Response register(UserDTO userRegister) {
        if (userRegister.getUserPassword() == null || userRegister.getUserMail() == null || userRegister.getUserFirstName() == null ||userRegister.getUserLastName() == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("mail, firstName, lastName or password is missing").build();

        UserModel newUser = usersBean.register(userRegister);

        return Response.status(Response.Status.OK).entity(newUser).build();
    }

    @POST
    @Path("login")
    public Response login(UserDTO userLogin) {
        if (userLogin.getUserPassword() == null || userLogin.getUserMail() == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("mail or password is missing").build();

        UserModel user = usersBean.login(userLogin);

        if (user == null)
            return  Response.status(Response.Status.NOT_FOUND).entity("login failed").build();

        return Response.status(Response.Status.OK).entity(user).build();
    }


    @PUT
    public Response update(UserDTO userUpdate) {
        if (userUpdate.getUserId() == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("object id is missing").build();

        UserModel user = usersBean.update(userUpdate);

        if (user == null)
            return  Response.status(Response.Status.NOT_FOUND).entity("no user found for update").build();

        return Response.status(Response.Status.OK).entity(user).build();
    }

    @DELETE
    @Path("{userId}")
    public Response delete(@PathParam("userId") Integer userId) {
        boolean res = usersBean.delete(userId);

        if (!res)
            Response.status(200).entity("No user deleted").build();

        return Response.status(201).entity("deletion success").build();
    }
}
