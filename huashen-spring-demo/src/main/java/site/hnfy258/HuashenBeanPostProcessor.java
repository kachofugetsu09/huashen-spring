package site.hnfy258;

import java.lang.reflect.Method;

@Component("huashenBeanPostProcessor")
public class HuashenBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.println("初始化前");
        //初始化前 @PostConstruct
        for(Method method : bean.getClass().getDeclaredMethods()){
            if(method.isAnnotationPresent(PostConstruct.class)){
                try {
                    method.invoke(bean);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("初始化后");
        return bean;
    }
}
