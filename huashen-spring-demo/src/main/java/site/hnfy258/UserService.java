package site.hnfy258;

import site.hnfy258.annotation.Autowired;
import site.hnfy258.annotation.Component;

@Component("userService")
public class UserService {
    @Autowired UserDao userDao;
    String name;
    private String uId;
    public UserService(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void queryUserInfo(String uId) {
        System.out.println("查询用户信息：" + userDao.queryUserName(uId));
    }
}
