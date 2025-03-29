package site.hnfy258.bean.context;

import site.hnfy258.bean.context.event.ApplicationEvent;

import java.util.EventListener;

public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

    void onApplicationEvent(E event);
}
