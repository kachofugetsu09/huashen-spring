package site.hnfy258.bean.config.InstantiationStrategy;

import site.hnfy258.bean.config.BeanDefinition;

import java.lang.reflect.Constructor;

public class SmartInstantiationStrategy implements InstantiationStrategy {
    private final JdkInstantiationStrategy jdkStrategy = new JdkInstantiationStrategy();
    private final CglibSubclassingInstantiationStrategy cglibStrategy = new CglibSubclassingInstantiationStrategy();

    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args) {
        Class<?> beanClass = beanDefinition.getType();
        
        // 如果类实现了至少一个接口，使用JDK动态代理
        if (beanClass.getInterfaces().length > 0) {
            return jdkStrategy.instantiate(beanDefinition, beanName, ctor, args);
        } 
        // 否则使用CGLIB
        else {
            return cglibStrategy.instantiate(beanDefinition, beanName, ctor, args);
        }
    }
}