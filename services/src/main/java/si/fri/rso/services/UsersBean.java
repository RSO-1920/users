package si.fri.rso.services;


import org.eclipse.jetty.server.Authentication;
import si.fri.rso.lib.UserModel;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UsersBean {
    private List<UserModel> users;

    @PostConstruct
    private void init() {
        System.out.println("Users service");
        users = new ArrayList<>();

        users.add(new UserModel(1, "Joža", "Novak", "jazsemjoza@gmail.com", "zorogaseka"));
        users.add(new UserModel(2, "Uros", "Zoretic", "zoreticu@gmail.com", "jstgasekam"));
    }

    public List<UserModel> getAllUsers() {
        System.out.println("Getting all users");
        return users;
    }
}
