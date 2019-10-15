package si.fri.rso.services;

import si.fri.rso.lib.UserDTO;
import si.fri.rso.lib.UserModel;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UsersBean {
    private List<UserModel> users;

    @PostConstruct
    private void init() {
        users = new ArrayList<UserModel>();

        users.add(new UserModel(1, "Joža", "Novak", "jazsemjoza@gmail.com", "zorogaseka"));
        users.add(new UserModel(2, "Uros", "Zoretic", "zoreticu@gmail.com", "jstgasekam"));
    }

    public List<UserModel> getAllUsers() {
        System.out.println("Getting all users");
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

}
