package si.fri.rso.services;

import si.fri.rso.lib.UserModel;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.ClientBuilder;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UsersObject {
    private List<UserModel> users;
    @PostConstruct
    private void init() {
        users = new ArrayList<UserModel>();

        users.add(new UserModel("1", "Joža", "Novak", "jazsemjoza@gmail.com", "zorogaseka"));
        users.add(new UserModel("2", "Uros", "Zoretic", "zoreticu@gmail.com", "jstgasekam"));
    }

    public List<UserModel> getUsers() {
        return this.users;
    }

    public void removeUser(UserModel user) {
        this.users.remove(user);
    }

    public void addUser(UserModel user) {
        this.users.add(user);
    }

}
