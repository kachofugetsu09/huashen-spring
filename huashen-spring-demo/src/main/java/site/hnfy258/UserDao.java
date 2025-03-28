package site.hnfy258;

import site.hnfy258.annotation.Component;

import java.util.HashMap;
import java.util.Map;

@Component("userDao")
public class UserDao {
    private static Map<String, String> hashMap = new HashMap<>();

    static {
        hashMap.put("10001", "花神");
        hashMap.put("10002", "huashen");
        hashMap.put("10003", "Limboo");
    }

    public String queryUserName(String uId) {
        return hashMap.get(uId);
    }
}
