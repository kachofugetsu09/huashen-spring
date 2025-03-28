package site.hnfy258.bean.factory;

import site.hnfy258.bean.config.BeanPostProcessor;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBeanFactory implements BeanFactory {
    protected List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();
    
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessorList.add(beanPostProcessor);
    }

    protected void applyBeanPostProcessorsBeforeInitialization(Object bean, String beanName) {
        for (BeanPostProcessor processor : beanPostProcessorList) {
            processor.postProcessBeforeInitialization(bean, beanName);
        }
    }

    protected void applyBeanPostProcessorsAfterInitialization(Object bean, String beanName) {
        for (BeanPostProcessor processor : beanPostProcessorList) {
            processor.postProcessAfterInitialization(bean, beanName);
        }
    }
}