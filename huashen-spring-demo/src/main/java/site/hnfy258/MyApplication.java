package site.hnfy258;

public class MyApplication {
    public static void main(String[] args) {
        HuashenApplicationContext context = new HuashenApplicationContext(MyConfig.class);
        HuashenApplicationContext context2 = new HuashenApplicationContext("classpath:spring.xml");
        TestBean testBean = (TestBean) context2.getBean("testBean");
        System.out.println("testBean:" + testBean);
        HuashenService huashenService = (HuashenService) context.getBean("huashenService");
        huashenService.useUserService();
        System.out.println("huashenService:" + huashenService);
        UserService userService = (UserService) context.getBean("userService");
        userService.queryUserInfo("10001");
        huashenService.test();

    }
}
