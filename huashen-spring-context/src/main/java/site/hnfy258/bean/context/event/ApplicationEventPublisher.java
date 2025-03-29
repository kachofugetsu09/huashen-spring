package site.hnfy258.bean.context.event;

public interface ApplicationEventPublisher {
    /**
     * 发布应用事件
     * @param event 要发布的事件
     */
    void publishEvent(ApplicationEvent event);
}