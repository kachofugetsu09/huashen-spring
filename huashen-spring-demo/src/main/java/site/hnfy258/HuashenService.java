package site.hnfy258;

@Component("huashenService")
@Scope("prototype")
public class HuashenService  implements InitializingBean{
    @Autowired UserService userService;
    public void test(){
        System.out.println(userService);
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
