package site.hnfy258.bean.config.InstantiationStrategy;

import java.lang.reflect.Constructor;
import net.sf.cglib.proxy.*;
import site.hnfy258.bean.config.BeanDefinition;

public class CglibSubclassingInstantiationStrategy implements InstantiationStrategy {
    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(beanDefinition.getType());
        enhancer.setCallback(new NoOp() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }
        });
        if(ctor == null){
            return enhancer.create();
        }
        return enhancer.create(ctor.getParameterTypes(), args);
    }
}
