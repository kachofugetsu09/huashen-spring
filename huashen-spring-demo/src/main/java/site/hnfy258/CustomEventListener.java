package site.hnfy258;

import site.hnfy258.annotation.Component;
import site.hnfy258.bean.context.ApplicationListener;
@Component("customEventListener")
public class CustomEventListener implements ApplicationListener<CustomEvent> {
    @Override
    public void onApplicationEvent(CustomEvent event) {
        System.out.println("收到自定义事件: " + event.getMessage());
    }
}