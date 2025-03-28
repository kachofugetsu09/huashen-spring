package site.hnfy258;

public class MyApplication {
    public static void main(String[] args) {
        HuashenApplicationContext context = new HuashenApplicationContext(MyConfig.class);
        HuashenService huashenService = (HuashenService) context.getBean("huashenService");
        System.out.println("huashenService:" + huashenService);
        huashenService.test();

    }
}
