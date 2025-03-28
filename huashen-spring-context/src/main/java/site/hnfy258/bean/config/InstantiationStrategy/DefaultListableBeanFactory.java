package site.hnfy258.bean.config.InstantiationStrategy;

import lombok.Getter;
import lombok.Setter;
import site.hnfy258.annotation.Autowired;
import site.hnfy258.bean.config.*;
import site.hnfy258.bean.factory.AbstractAutowireCapableBeanFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
@Getter
@Setter
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory {
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    private Map<String, Object> singletonObjects = new HashMap<>();
    private InstantiationStrategy instantiationStrategy = new SmartInstantiationStrategy();

    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
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

    public Object getBean(String name) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(name);
        if (beanDefinition == null) {
            throw new RuntimeException("No bean named '" + name + "' is defined");
        }

        if (beanDefinition.getScope().equals("singleton")) {
            Object singletonObject = singletonObjects.get(name);
            if (singletonObject == null) {
                singletonObject = createBean(name, beanDefinition);
                singletonObjects.put(name, singletonObject);
            }
            return singletonObject;
        }

        return createBean(name, beanDefinition);
    }

}
