package site.hnfy258;

public class MyApplication  {
    public static void main(String[] args) throws Exception {
        HuashenApplicationContext context = new HuashenApplicationContext(MyConfig.class);
        // 添加测试事件的部分
        System.out.println("\n===== 测试事件机制 =====");
        // 直接使用context发布事件，而不是通过listener
        context.publishEvent(new CustomEvent(context, "这是一个测试事件"));
        context.close();
        HuashenApplicationContext context2 = new HuashenApplicationContext("classpath:spring.xml");
        TestBean testBean = (TestBean) context2.getBean("testBean");
        System.out.println("testBean:" + testBean);
        HuashenService huashenService = (HuashenService) context.getBean("huashenService");
        HuashenService huashenService2 = (HuashenService) context.getBean("huashenService");
        huashenService.useUserService();
        System.out.println("huashenService:" + huashenService);
        System.out.println("huashenService2:" + huashenService2);
        UserService userService = (UserService) context.getBean("userService");
        userService.queryUserInfo("10001");
        huashenService.test();


        System.out.println("Testing ApplicationContextAware functionality:");
        AwareTestBean awareTestBean = (AwareTestBean) context.getBean("awareTestBean");
        awareTestBean.testAwareFunction();

    }
}
