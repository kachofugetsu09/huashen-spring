package site.hnfy258;

import site.hnfy258.annotation.Autowired;
import site.hnfy258.annotation.Component;
import site.hnfy258.annotation.PostConstruct;
import site.hnfy258.annotation.Scope;
import site.hnfy258.bean.config.InitializingBean;

@Component("huashenService")
@Scope("prototype")
public class HuashenService  implements InitializingBean {
    @Autowired
    UserService userService;


    public void test(){
        System.out.println(userService);
    }

    public void useUserService(){
        System.out.println(userService.getName());
    }


    @PostConstruct
    public void a(){
        System.out.println("huashen1");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("huashen2");
    }

}
