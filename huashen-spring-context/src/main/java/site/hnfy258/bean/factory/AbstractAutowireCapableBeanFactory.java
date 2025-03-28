package site.hnfy258.bean.factory;

import lombok.Getter;
import lombok.Setter;
import site.hnfy258.bean.config.*;
import site.hnfy258.bean.config.InstantiationStrategy.*;

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
            // 首先尝试获取类的构造函数
            Class<?> beanClass = beanDefinition.getType();
            if (beanClass.getConstructors().length > 0) {
                // 使用第一个可用的构造函数
                java.lang.reflect.Constructor<?> constructor = beanClass.getConstructors()[0];
                // 为构造函数准备参数
                Object[] args = new Object[constructor.getParameterCount()];
                // 为构造函数参数提供默认值（实际应用中应该从容器中查找依赖）
                for (int i = 0; i < args.length; i++) {
                    Class<?> paramType = constructor.getParameterTypes()[i];
                    // 设置一些基本默认值
                    if (paramType == String.class) {
                        args[i] = "";
                    } else if (paramType.isPrimitive()) {
                        args[i] = 0;  // 基本类型默认值
                    } else {
                        args[i] = null;
                    }
                }
                bean = createBeanInstance(beanDefinition, beanName, args);
            } else {
                bean = createBeanInstance(beanDefinition);
            }
        } catch (Exception e) {
            bean = createBeanInstance(beanDefinition);
        }

        applyPropertyValues(beanName, bean, beanDefinition);
        initializeBean(beanName, bean, beanDefinition);
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