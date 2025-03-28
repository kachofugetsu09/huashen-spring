package site.hnfy258.bean.factory;

import site.hnfy258.bean.config.BeanDefinition;

import java.util.Objects;

public interface BeanFactory {
    Object createBean(String beanName, BeanDefinition beanDefinition);
    Object getBean(String name) throws Exception;
}