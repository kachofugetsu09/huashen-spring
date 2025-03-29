package site.hnfy258;

import site.hnfy258.annotation.Component;

@Component("testService")
public class TestService implements ITestService {
    
    public void doSomething() {
        System.out.println("TestService is doing something...");
    }
    
    public String getInfo(String param) {
        System.out.println("TestService is getting info with param: " + param);
        return "Info: " + param;
    }
}
