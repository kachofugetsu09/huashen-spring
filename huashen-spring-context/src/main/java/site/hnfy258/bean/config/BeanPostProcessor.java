package site.hnfy258.bean.config;

import site.hnfy258.exception.BeansException;

public interface BeanPostProcessor {

    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    default Object postProcessAfterInitialization(Object bean, String beanName){return bean;}
}
