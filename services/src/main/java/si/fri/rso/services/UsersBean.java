package si.fri.rso.services;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import si.fri.rso.config.UsersConfigProperties;
import si.fri.rso.lib.ChannelDTO;
import si.fri.rso.lib.ResponseDTO;
import si.fri.rso.lib.UserDTO;
import si.fri.rso.lib.UserModel;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
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

@ApplicationScoped
public class UsersBean {

    @Inject
    private UsersConfigProperties configProperties;

    @Inject
    @DiscoverService(value = "rso1920-channels")
    private Optional<String> channelsUrl;

    private List<UserModel> users;

    private Client httpClient;

    @PostConstruct
    private void init() {
        users = new ArrayList<UserModel>();

        users.add(new UserModel(1, "Joža", "Novak", "jazsemjoza@gmail.com", "zorogaseka"));
        users.add(new UserModel(2, "Uros", "Zoretic", "zoreticu@gmail.com", "jstgasekam"));

        this.httpClient = ClientBuilder.newClient();
    }

    public List<UserModel> getAllUsers() {
        return users;
    }

    public UserModel getUser(Integer userId) {

        for (UserModel user : users) {
            if (user.getUser_id().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    public UserModel login(UserDTO userLogin) {

        for (UserModel user : users) {
            if (user.getUser_mail().equals(userLogin.getUserMail()) && user.getUser_password().equals(userLogin.getUserPassword()) )
                return user;
        }
        return null;
    }

    public boolean delete(Integer userId) {
        for (UserModel user : users) {
            if (user.getUser_id().equals(userId)) {
                users.remove(user);
                return true;
            }
        }
        return false;
    }

    public ResponseDTO register(UserDTO userRegister) {
        System.out.println("url: " + this.channelsUrl);
        Integer id = this.users.get(this.users.size() - 1).getUser_id()  + 1;

        UserModel newUser = new UserModel(id, userRegister.getUserFirstName(), userRegister.getUserLastName(), userRegister.getUserMail(), userRegister.getUserPassword());
        this.users.add(newUser);

        if (!this.channelsUrl.isPresent()) {
            return new ResponseDTO(200, "no base url for channel api", newUser);
        }

        System.out.println("Config channel url: " + this.channelsUrl.get() + this.configProperties.getChannelApiAddChannelPath());

        ChannelDTO userChannel = new ChannelDTO();
        userChannel.setChannelName("channel-"+newUser.getUser_last_name());
        userChannel.setAdminId(newUser.getUser_id());
        userChannel.setChannelType(1);

        try{
            Response success = this.httpClient
                    .target(this.channelsUrl.get() + this.configProperties.getChannelApiAddChannelPath())
                    .request(MediaType.APPLICATION_JSON_TYPE).post( Entity.entity(userChannel, MediaType.APPLICATION_JSON_TYPE));

            System.out.println("Channel creation status: " + success.getStatus());
            if (success.getStatus() == 200) {
                System.out.println("User channel creation success");
                return new ResponseDTO(200, "channel creation success", newUser);
            } else {
                return new ResponseDTO(200, "channel creation failed", newUser);
            }
        }catch (WebApplicationException | ProcessingException e) {
            e.printStackTrace();
            return new ResponseDTO(200, "api for creating channel not reachable", newUser);
        }
    }

    public UserModel update(UserDTO userUpdate) {
        for (UserModel user : users) {
            if (user.getUser_id().equals(userUpdate.getUserId())) {
                // update userProperties

                if (userUpdate.getUserFirstName() != null)
                    user.setUser_first_name(userUpdate.getUserFirstName());

                if (userUpdate.getUserLastName() != null)
                    user.setUser_last_name(userUpdate.getUserLastName());

                if (userUpdate.getUserMail() != null)
                    user.setUser_mail(userUpdate.getUserMail());

                if (userUpdate.getUserPassword() != null)
                    user.setUser_password(userUpdate.getUserPassword());

                return user;
            }
        }

        return null;
    }
}
