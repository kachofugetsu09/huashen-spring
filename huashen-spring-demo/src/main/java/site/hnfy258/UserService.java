package site.hnfy258;

import site.hnfy258.annotation.Component;

@Component("userService")
public class UserService {
    String name;
    public UserService(String name) {
        this.name = name;
    }
}
