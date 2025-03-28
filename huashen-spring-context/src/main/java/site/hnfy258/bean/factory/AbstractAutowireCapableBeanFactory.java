package site.hnfy258.bean.factory;

import lombok.Getter;
import lombok.Setter;
import site.hnfy258.bean.config.*;
import site.hnfy258.bean.config.InstantiationStrategy.*;

import java.lang.reflect.Constructor;

@Setter
@Getter
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {
    private InstantiationStrategy instantiationStrategy = new SmartInstantiationStrategy();
    private ConstructorResolver constructorResolver;
    private ArgumentResolver argumentResolver;

    public AbstractAutowireCapableBeanFactory() {
        this.constructorResolver = new DefaultConstructorResolver();
        this.argumentResolver = new DefaultArgumentResolver();
    }
    @Override
    public Object createBean(String beanName, BeanDefinition beanDefinition) {
        Object bean = null;
        try {
            Class<?> beanClass = beanDefinition.getType();

            Constructor<?> constructor = constructorResolver.resolveConstructor(beanDefinition);

            if (constructor != null) {
                Object[] args = argumentResolver.resolveConstructorArguments(constructor, beanDefinition);
                bean = createBeanInstance(beanDefinition, beanName, args);
            } else {
                bean = createBeanInstance(beanDefinition);
            }

            applyPropertyValues(beanName, bean, beanDefinition);
            initializeBean(beanName, bean, beanDefinition);
        } catch (Exception e) {
            throw new RuntimeException("Error creating bean with name '" + beanName + "'", e);
        }
        return bean;
    }

    private Object createBeanInstance(BeanDefinition beanDefinition) {
        return getInstantiationStrategy().instantiate(beanDefinition, beanDefinition.getClass().getName(), null, null);
    }


    protected abstract Object createBeanInstance(BeanDefinition beanDefinition, String beanName, Object[] args);

    protected abstract void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition);
    
    protected void initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
        applyBeanPostProcessorsBeforeInitialization(bean, beanName);
        invokeInitMethods(bean, beanName);
        applyBeanPostProcessorsAfterInitialization(bean, beanName);
    }

    private void invokeInitMethods(Object bean, String beanName) {
        if (bean instanceof InitializingBean) {
            ((InitializingBean) bean).afterPropertiesSet();
        }
    }
}