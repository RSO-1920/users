package si.fri.rso.services;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsersBean {
    @PostConstruct
    private void init() {
        System.out.println("Users service");
    }

    public void getAllUsers() {
        System.out.println("Getting all users");
    }
}
