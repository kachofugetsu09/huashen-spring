package site.hnfy258.bean.factory;

import cn.hutool.core.util.ClassUtil;
import lombok.Getter;
import lombok.Setter;
import site.hnfy258.bean.config.*;
import site.hnfy258.bean.config.InstantiationStrategy.*;
import site.hnfy258.bean.factory.aware.BeanClassLoaderAware;
import site.hnfy258.bean.factory.aware.BeanFactoryAware;
import site.hnfy258.bean.factory.aware.BeanNameAware;
import site.hnfy258.exception.BeansException;

import java.lang.reflect.Constructor;

@Setter
@Getter
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {
    private InstantiationStrategy instantiationStrategy = new SmartInstantiationStrategy();
    private ConstructorResolver constructorResolver;
    private ArgumentResolver argumentResolver;

    private ClassLoader classLoader;

    public AbstractAutowireCapableBeanFactory() {
        this.constructorResolver = new DefaultConstructorResolver();
        this.argumentResolver = new DefaultArgumentResolver();
        classLoader = ClassUtil.getClassLoader();
    }
    @Override
    public Object createBean(String beanName, BeanDefinition beanDefinition) {
        Object bean = null;
        try {
            Class<?> beanClass = beanDefinition.getType();
            //查找合适的构造函数
            Constructor<?> constructor = constructorResolver.resolveConstructor(beanDefinition);

            if (constructor != null) {
                //填入默认值，以完成默认构造
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

    protected void invokeAwareMethods(final String beanName, final Object bean) {
        if (bean instanceof BeanNameAware) {
            ((BeanNameAware) bean).setBeanName(beanName);
        }
        if (bean instanceof BeanClassLoaderAware) {
            ((BeanClassLoaderAware) bean).setBeanClassLoader(getBeanClassLoader());
        }
        if (bean instanceof BeanFactoryAware) {
            ((BeanFactoryAware) bean).setBeanFactory(this);
        }
    }

    private ClassLoader getBeanClassLoader() {
        return classLoader;
    }


    private Object createBeanInstance(BeanDefinition beanDefinition) {
        return getInstantiationStrategy().instantiate(beanDefinition, beanDefinition.getClass().getName(), null, null);
    }


    protected abstract Object createBeanInstance(BeanDefinition beanDefinition, String beanName, Object[] args);

    protected abstract void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition);
    
    protected void initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) throws BeansException {
        invokeAwareMethods(beanName, bean);
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