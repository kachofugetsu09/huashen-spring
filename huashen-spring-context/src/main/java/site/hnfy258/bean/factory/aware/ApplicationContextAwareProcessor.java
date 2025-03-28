package site.hnfy258.bean.factory.aware;

import lombok.Getter;
import lombok.Setter;
import site.hnfy258.bean.ApplicationContext;
import site.hnfy258.bean.config.BeanPostProcessor;
import site.hnfy258.exception.BeansException;

@Setter
@Getter
public class ApplicationContextAwareProcessor implements BeanPostProcessor {

    private final ApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof  ApplicationContextAware){
            ((ApplicationContextAware) bean).setApplicationContext(applicationContext);
        }
        return bean;
    }

}
