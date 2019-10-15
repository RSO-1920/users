package si.fri.rso.services;

import si.fri.rso.lib.UserDTO;
import si.fri.rso.lib.UserModel;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UsersBean {
    private List<UserModel> users;

    private Client httpClient;
    private String baseUrl;

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

    public UserModel register(UserDTO userRegister) {
        Integer id = this.users.get(this.users.size() - 1).getUser_id()  + 1;

        UserModel newUser = new UserModel(id, userRegister.getUserFirstName(), userRegister.getUserLastName(), userRegister.getUserMail(), userRegister.getUserPassword());
        this.users.add(newUser);

        // TODO: call to channel api..

        return  newUser;
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
