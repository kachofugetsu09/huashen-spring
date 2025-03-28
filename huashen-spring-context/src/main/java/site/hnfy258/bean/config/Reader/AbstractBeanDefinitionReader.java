package site.hnfy258.bean.config.Reader;

import site.hnfy258.bean.core.io.DefaultResourceLoader;
import site.hnfy258.bean.core.io.Resource;
import site.hnfy258.bean.core.io.ResourceLoader;
import site.hnfy258.bean.factory.BeanFactory;

public  abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader{

    private final BeanFactory beanFactory;

    private ResourceLoader resourceLoader;



    protected AbstractBeanDefinitionReader(BeanFactory beanFactory){
        this.beanFactory = beanFactory;
        this.resourceLoader = new DefaultResourceLoader();
    }

    protected AbstractBeanDefinitionReader(BeanFactory beanFactory,ResourceLoader resourceLoader){
        this.beanFactory = beanFactory;
        this.resourceLoader = resourceLoader;
    }
    @Override
    public BeanFactory beanFactory() {
        return beanFactory;
    }

    @Override
    public ResourceLoader resourceLoader() {
        return resourceLoader;
    }

}
