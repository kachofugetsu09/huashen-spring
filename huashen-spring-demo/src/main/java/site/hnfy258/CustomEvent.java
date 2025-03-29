package site.hnfy258;

import site.hnfy258.bean.context.event.ApplicationEvent;

public class CustomEvent extends ApplicationEvent {
    private String message;
    
    public CustomEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}