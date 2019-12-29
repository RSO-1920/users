package si.fri.rso.services;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import si.fri.rso.config.UsersConfigProperties;
import si.fri.rso.lib.ChannelDTO;
import si.fri.rso.lib.ResponseDTO;
import si.fri.rso.lib.UserDTO;
import si.fri.rso.lib.UserModel;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestScoped
public class UsersBean {

    @Inject
    private UsersConfigProperties configProperties;

    @Inject UsersObject usersObject;

    @Inject
    @DiscoverService(value = "rso1920-channels")
    private Optional<String> channelsUrl;

    private Client httpClient;

    @PostConstruct
    private void init() {

        this.httpClient = ClientBuilder.newClient();
    }

    public UserModel login(UserDTO userLogin) {

        for (UserModel user : usersObject.getUsers()) {
            if (user.getUser_mail().equals(userLogin.getUserMail()) && user.getUser_password().equals(userLogin.getUserPassword()) )
                return user;
        }
        return null;
    }

    public ResponseDTO register(UserDTO userRegister, String uniqueRequestId, String userId) {
        System.out.println("url: " + this.channelsUrl);

        UserModel newUser = new UserModel(userId, userRegister.getUserFirstName(), userRegister.getUserLastName(), userRegister.getUserMail(), userRegister.getUserPassword());
        this.usersObject.addUser(newUser);

        System.out.println("new user: ");
        System.out.println("REQUEST: " + uniqueRequestId);

        if (!this.channelsUrl.isPresent()) {
            return new ResponseDTO(400, "no base url for channel api", newUser);
        }

        System.out.println("Config channel url: " + this.channelsUrl.get() + this.configProperties.getChannelApiAddChannelPath());

        ChannelDTO userChannel = new ChannelDTO();
        userChannel.setChannelName("channel-"+newUser.getUser_last_name());
        userChannel.setAdminId(newUser.getUser_id());
        userChannel.setChannelType(1);

        try{
            Response success = this.httpClient
                    .target(this.channelsUrl.get() + this.configProperties.getChannelApiAddChannelPath())
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("uniqueRequestId", uniqueRequestId)
                    .post( Entity.entity(userChannel, MediaType.APPLICATION_JSON_TYPE));

            System.out.println("Channel creation status: " + success.getStatus());
            if (success.getStatus() == 200) {
                System.out.println("User channel creation success");
                return new ResponseDTO(200, "channel creation success", newUser);
            } else {
                return new ResponseDTO(400, "channel creation failed", newUser);
            }
        }catch (WebApplicationException | ProcessingException e) {
            e.printStackTrace();
            return new ResponseDTO(400, "api for creating channel not reachable", newUser);
        }
    }
}
