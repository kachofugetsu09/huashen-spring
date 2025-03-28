package site.hnfy258.bean.config.InstantiationStrategy;

import site.hnfy258.bean.config.BeanDefinition;

import java.lang.reflect.Constructor;

public class SimpleInstantiationStrategy implements InstantiationStrategy{
    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args) {
        Class type = beanDefinition.getType();
        try{
            if(ctor != null){
                return type.getConstructor(ctor.getParameterTypes()).newInstance(args);
            }else{
                return type.getDeclaredConstructor().newInstance();
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("实例化失败");
        }
        return null;
    }
}
