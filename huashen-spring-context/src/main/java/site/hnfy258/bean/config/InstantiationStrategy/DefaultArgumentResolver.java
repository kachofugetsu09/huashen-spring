package site.hnfy258.bean.config.InstantiationStrategy;

import site.hnfy258.bean.config.BeanDefinition;

import java.lang.reflect.Constructor;

public class DefaultArgumentResolver implements ArgumentResolver {
    @Override
    public Object[] resolveConstructorArguments(Constructor<?> constructor, BeanDefinition beanDefinition) {
        Class<?>[] paramTypes = constructor.getParameterTypes();
        Object[] args = new Object[paramTypes.length];
        
        for (int i = 0; i < args.length; i++) {
            args[i] = getDefaultValue(paramTypes[i]);
        }
        
        return args;
    }
    
    private Object getDefaultValue(Class<?> type) {
        if (type == String.class) {
            return "";
        } else if (type.isPrimitive()) {
            if (type == boolean.class) return false;
            if (type == char.class) return '\0';
            return 0;
        }
        return null;
    }
}
