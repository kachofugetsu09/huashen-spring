package site.hnfy258.bean;

import site.hnfy258.exception.BeansException;

public interface ConfigurableApplicationContext extends ApplicationContext {

    /**
     * 刷新容器
     *
     * @throws BeansException
     */
    void refresh() throws BeansException;


    void registerShutdownHook();

    void close();

}