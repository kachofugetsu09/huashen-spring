package site.hnfy258.bean.factory;

import site.hnfy258.bean.ConfigurableApplicationContext;
import site.hnfy258.bean.config.BeanDefinition;
import site.hnfy258.bean.core.io.DefaultResourceLoader;
import site.hnfy258.bean.factory.aware.ApplicationContextAwareProcessor;
import site.hnfy258.exception.BeansException;

import java.util.Map;

public  abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {
    @Override
    public void refresh() throws BeansException {
        getBeanFactory().addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
    }

    protected abstract ConfigurableListableBeanFactory getBeanFactory();


}
