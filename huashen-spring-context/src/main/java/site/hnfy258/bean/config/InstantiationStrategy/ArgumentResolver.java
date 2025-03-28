package site.hnfy258.bean.config.InstantiationStrategy;

import site.hnfy258.bean.config.BeanDefinition;

import java.lang.reflect.Constructor;

public interface ArgumentResolver {
    Object[] resolveConstructorArguments(Constructor<?> constructor, BeanDefinition beanDefinition);
}