package site.hnfy258.bean;

import site.hnfy258.bean.context.event.ApplicationEventPublisher;
import site.hnfy258.bean.factory.ListableBeanFactory;

public interface ApplicationContext extends ListableBeanFactory, ApplicationEventPublisher {
}