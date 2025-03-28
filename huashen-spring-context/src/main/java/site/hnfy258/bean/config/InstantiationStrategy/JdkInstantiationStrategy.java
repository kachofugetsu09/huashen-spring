package site.hnfy258.bean.config.InstantiationStrategy;

import site.hnfy258.bean.config.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class JdkInstantiationStrategy implements InstantiationStrategy {
    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args) {
        Class<?> clazz = beanDefinition.getType();
        try {
            if (ctor != null) {
                return ctor.newInstance(args);
            } else {
                return clazz.getDeclaredConstructor().newInstance();
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Failed to instantiate [" + clazz.getName() + "]", e);
        }
    }

}