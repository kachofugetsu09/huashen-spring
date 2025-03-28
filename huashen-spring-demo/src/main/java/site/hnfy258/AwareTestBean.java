package site.hnfy258;

import site.hnfy258.annotation.Component;
import site.hnfy258.bean.ApplicationContext;
import site.hnfy258.bean.factory.aware.ApplicationContextAware;
@Component("awareTestBean")
public class AwareTestBean implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        System.out.println("setApplicationContext called with: " + applicationContext);
    }

    public void testAwareFunction() {
        if (applicationContext != null) {
            System.out.println("ApplicationContext is successfully injected: " + applicationContext);
            try {
                UserService userService = (UserService) applicationContext.getBean("userService");
                System.out.println("UserService from context: " + userService);
            } catch (Exception e) {
                System.out.println("Error getting UserService: " + e.getMessage());
            }
        } else {
            System.out.println("ApplicationContext injection failed.");
        }
    }
}
