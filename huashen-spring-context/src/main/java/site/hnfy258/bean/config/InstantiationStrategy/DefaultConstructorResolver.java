package site.hnfy258.bean.config.InstantiationStrategy;

import site.hnfy258.bean.config.BeanDefinition;

import java.lang.reflect.Constructor;

public class DefaultConstructorResolver implements ConstructorResolver {
    @Override
    public Constructor<?> resolveConstructor(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getType();
        Constructor<?>[] constructors = beanClass.getConstructors();
        
        if (constructors.length > 0) {
            return constructors[0];
        }
        return null;
    }
}