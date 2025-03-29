package site.hnfy258.bean.context;

import site.hnfy258.bean.ConfigurableApplicationContext;
import site.hnfy258.bean.config.BeanDefinition;
import site.hnfy258.bean.context.event.*;
import site.hnfy258.bean.core.io.DefaultResourceLoader;
import site.hnfy258.bean.factory.DefaultListableBeanFactory;
import site.hnfy258.exception.BeansException;

import java.util.Collection;


public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {
    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    private ApplicationEventMulticaster applicationEventMulticaster;
    private DefaultListableBeanFactory beanFactory;

    public AbstractApplicationContext() {
        this.beanFactory = new DefaultListableBeanFactory();

    }

    @Override
    public void refresh() throws BeansException {
        initApplicationEventMulticaster();

        registerListener();

        finishRefresh();

    }

    private void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));
    }

    public void publishEvent(ApplicationEvent event) {
        applicationEventMulticaster.multicastEvent(event);
    }


    private void registerListener() throws BeansException {
        Collection<ApplicationListener> applicationListeners = getBeansOfType(ApplicationListener.class).values();
        for (ApplicationListener listener : applicationListeners) {
            applicationEventMulticaster.addApplicationListener(listener);
        }
    }


    private void initApplicationEventMulticaster(){
        applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.registerBeanDefinition(APPLICATION_EVENT_MULTICASTER_BEAN_NAME,
                new BeanDefinition(SimpleApplicationEventMulticaster.class));
    }

    @Override
    public void close() {
        publishEvent(new ContextClosedEvent(this));
    }

    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }


}
