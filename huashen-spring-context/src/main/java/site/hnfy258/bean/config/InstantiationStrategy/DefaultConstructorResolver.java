package site.hnfy258.bean.config.InstantiationStrategy;

import site.hnfy258.bean.config.BeanDefinition;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;

public class DefaultConstructorResolver implements ConstructorResolver {
    @Override
    public Constructor<?> resolveConstructor(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getType();

        try {
            Constructor<?> noArgConstructor = beanClass.getDeclaredConstructor();
            if (noArgConstructor != null) {
                return noArgConstructor;
            }
        } catch (NoSuchMethodException e) {
        }

        Constructor<?>[] publicConstructors = beanClass.getConstructors();

        if (publicConstructors.length == 0) {
            Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
            if (declaredConstructors.length > 0) {
                // 选择参数最少的构造函数
                Arrays.sort(declaredConstructors, Comparator.comparingInt(Constructor::getParameterCount));
                return declaredConstructors[0];
            }
            return null;
        }

        Arrays.sort(publicConstructors, Comparator.comparingInt(Constructor::getParameterCount));
        return publicConstructors[0];
    }
}