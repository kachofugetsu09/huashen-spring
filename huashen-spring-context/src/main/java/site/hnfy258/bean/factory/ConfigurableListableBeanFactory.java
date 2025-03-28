package site.hnfy258.bean.factory;

import site.hnfy258.bean.config.BeanDefinition;
import site.hnfy258.bean.config.BeanPostProcessor;
import site.hnfy258.exception.BeansException;

public interface ConfigurableListableBeanFactory extends ListableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

}