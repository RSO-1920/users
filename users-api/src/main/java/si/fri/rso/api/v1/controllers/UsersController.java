package si.fri.rso.api.v1.controllers;

import com.kumuluz.ee.logs.cdi.Log;
import javassist.NotFoundException;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;
import si.fri.rso.lib.ResponseDTO;
import si.fri.rso.lib.UserDTO;
import si.fri.rso.lib.UserModel;
import si.fri.rso.services.UsersBean;
import si.fri.rso.services.KeycloakBean;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/users")
public class UsersController {

    @Inject
    private UsersBean usersBean;

    @Inject
    HttpServletRequest requestheader;

    @Inject
    KeycloakBean keycloakBean;

    @GET
    @Timed(name = "users_time_all")
    @Counted(name = "users_counted_all")
    @Metered(name = "users_metered_all")
    public Response getUsers() {
        List<UserModel> users = usersBean.getAllUsers();

        ResponseDTO responseDTO = new ResponseDTO(200, "", users);

        /*try{
            return Response.status(Response.Status.OK).entity(keycloakBean.getAllUsers().toList()).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.status(Response.Status.OK).entity("err").build();
        */
        return Response.status(Response.Status.OK).entity(responseDTO).build();
    }

    @GET
    @Timed(name = "users_time_userId")
    @Counted(name = "users_counted_userId")
    @Metered(name = "users_metered_userId")
    @Path("{userId}")
    public Response getUser(@PathParam("userId") Integer userId) {
        UserModel user = usersBean.getUser(userId);

        ResponseDTO responseDTO = new ResponseDTO(200, "", user);

        return Response.status(Response.Status.OK).entity(responseDTO).build();
    }

    @POST
    @Timed(name = "users_time_register")
    @Counted(name = "users_counted_register")
    @Metered(name = "users_metered_register")
    @Path("register")
    public Response register(UserDTO userRegister) {
        if (userRegister.getUserName() == null || userRegister.getUserPassword() == null || userRegister.getUserMail() == null || userRegister.getUserFirstName() == null ||userRegister.getUserLastName() == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseDTO(400, "No password, mail, name or lastname given", new Object())).build();

        try{
            String createsUser = keycloakBean.createUser(userRegister.getUserName(), userRegister.getUserFirstName(), userRegister.getUserLastName(),  userRegister.getUserPassword(), userRegister.getUserMail());
            if(createsUser.length() > 0){
                System.out.println("ERROR: " + createsUser);
            }
            else{
                String createdKeycloakID = keycloakBean.getUserID(userRegister.getUserName());
                System.out.println("Keycloak user ID: " + createdKeycloakID); // TODO: <--------- Zoro tle mas ID na novo ustvarjenega keycloak userja
            }
        }
        catch (IOException | NotFoundException e){
            System.out.println(e);
        }

        String requestHeader = requestheader.getHeader("uniqueRequestId");
        System.out.println("HEADER: " + requestHeader);

        ResponseDTO responseDTO =  usersBean.register(userRegister, requestHeader != null ? requestHeader : UUID.randomUUID().toString());

        return Response.status(Response.Status.OK).entity(responseDTO).build();
    }


    // TODO KLIC STORITVE ZA jwt TOKEN, KI SE BO POTEM POŠILJAL OKUL.
    @POST
    @Timed(name = "users_time_login")
    @Counted(name = "users_counted_login")
    @Metered(name = "users_metered_login")
    @Path("login")
    public Response login(UserDTO userLogin) {
        if (userLogin.getUserPassword() == null || userLogin.getUserMail() == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseDTO(400, "mail or password is missing", new Object())).build();

        UserModel user = usersBean.login(userLogin);

        if (user == null)
            return  Response.status(Response.Status.NOT_FOUND).entity(new ResponseDTO(404, "login failed", new Object())).build();

        ResponseDTO responseDTO = new ResponseDTO(200, "", user);

        return Response.status(Response.Status.OK).entity(responseDTO).build();
    }


    @PUT
    @Timed(name = "users_time_update")
    @Counted(name = "users_counted_update")
    @Metered(name = "users_metered_update")
    public Response update(UserDTO userUpdate) {
        if (userUpdate.getUserId() == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseDTO(400, "object id is missing", new Object())).build();

        UserModel user = usersBean.update(userUpdate);

        if (user == null)
            return  Response.status(Response.Status.NOT_FOUND).entity(new ResponseDTO(404, "no user found for update", new Object())).build();

        return Response.status(Response.Status.OK).entity(new ResponseDTO(200, "", user)).build();
    }

    @DELETE
    @Timed(name = "users_time_delete")
    @Counted(name = "users_counted_delete")
    @Metered(name = "users_metered_delete")
    @Path("{userId}")
    public Response delete(@PathParam("userId") Integer userId) {
        boolean res = usersBean.delete(userId);

        if (!res)
            Response.status(200).entity(new ResponseDTO(200, "no user deleted", new Object())).build();

        return Response.status(201).entity(new ResponseDTO(404, "User deleted with id: "+userId, new Object())).build();
    }
}
