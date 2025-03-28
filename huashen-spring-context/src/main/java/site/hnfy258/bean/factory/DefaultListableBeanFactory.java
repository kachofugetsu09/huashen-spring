package site.hnfy258.bean.factory;

import lombok.Getter;
import lombok.Setter;
import site.hnfy258.annotation.Autowired;
import site.hnfy258.bean.config.*;
import site.hnfy258.bean.config.registry.BeanDefinitionRegistry;
import site.hnfy258.bean.config.InstantiationStrategy.*;
import site.hnfy258.bean.config.registry.DefaultBeanDefinitionRegistry;
import site.hnfy258.bean.config.registry.DefaultSingletonBeanRegistry;
import site.hnfy258.bean.config.registry.SingletonBeanRegistry;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory  {
    private final BeanDefinitionRegistry beanDefinitionRegistry;
    private final SingletonBeanRegistry singletonBeanRegistry;
    private InstantiationStrategy instantiationStrategy = new SmartInstantiationStrategy();

    public DefaultListableBeanFactory() {
        beanDefinitionRegistry = new DefaultBeanDefinitionRegistry();
        singletonBeanRegistry = new DefaultSingletonBeanRegistry();
    }

    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinition);
    }

    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitionRegistry.getBeanDefinition(beanName);
    }

    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionRegistry.containsBeanDefinition(beanName);
    }

    public String[] getBeanDefinitionNames() {
        return beanDefinitionRegistry.getBeanDefinitionNames();
    }



    public void useJdkInstantiationStrategy() {
        setInstantiationStrategy(new JdkInstantiationStrategy());
    }

    public void useCglibInstantiationStrategy() {
        setInstantiationStrategy(new CglibSubclassingInstantiationStrategy());
    }

    public void useSmartInstantiationStrategy() {
        setInstantiationStrategy(new SmartInstantiationStrategy());
    }

    @Override
    protected Object createBeanInstance(BeanDefinition beanDefinition, String beanName, Object[] args) {
        Constructor<?> constructorToUse = null;
        Class<?> beanClass = beanDefinition.getType();
        Constructor<?>[] ctors = beanClass.getDeclaredConstructors();

        constructorToUse = getConstructorToUse(beanName, args, ctors, constructorToUse);

        return getInstantiationStrategy().instantiate(beanDefinition, beanName, constructorToUse, args);
    }

    private static Constructor<?> getConstructorToUse(String beanName, Object[] args, Constructor<?>[] ctors, Constructor<?> constructorToUse) {
        if (args != null) {
            for (Constructor<?> ctor : ctors) {
                if (ctor.getParameterTypes().length == args.length) {
                    boolean match = true;
                    for (int i = 0; i < args.length; i++) {
                        if (args[i] != null && !ctor.getParameterTypes()[i].isInstance(args[i])) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        constructorToUse = ctor;
                        break;
                    }
                }
            }
        }

        if (constructorToUse == null) {
            for (Constructor<?> ctor : ctors) {
                if (ctor.getParameterTypes().length == 0) {
                    constructorToUse = ctor;
                    break;
                }
            }
        }

        if (constructorToUse == null) {
            throw new RuntimeException("No suitable constructor found for bean '" + beanName + "'");
        }
        return constructorToUse;
    }

    @Override
    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        for (Field field : beanDefinition.getType().getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                String autowiredBeanName = field.getName();
                Object autowiredBean = getBean(autowiredBeanName);
                field.setAccessible(true);
                try {
                    field.set(bean, autowiredBean);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to set autowired field", e);
                }
            }
        }
    }

    @Override
    public Object getBean(String name) {
        BeanDefinition beanDefinition = getBeanDefinition(name);
        if (beanDefinition.getScope().equals("singleton")) {
            Object singletonObject = singletonBeanRegistry.getSingleton(name);
            if (singletonObject == null) {
                singletonObject = createBean(name, beanDefinition);
                singletonBeanRegistry.registerSingleton(name, singletonObject);
            }
            return singletonObject;
        }

        return createBean(name, beanDefinition);
    }
}