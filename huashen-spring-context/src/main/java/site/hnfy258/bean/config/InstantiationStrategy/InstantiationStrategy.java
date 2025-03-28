package site.hnfy258.bean.config.InstantiationStrategy;

import site.hnfy258.bean.config.BeanDefinition;

import java.lang.reflect.Constructor;

public interface InstantiationStrategy {
    Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args);
}
