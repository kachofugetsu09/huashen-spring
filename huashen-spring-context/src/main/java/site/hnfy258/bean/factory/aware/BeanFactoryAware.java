package site.hnfy258.bean.factory.aware;

import site.hnfy258.bean.factory.BeanFactory;

public interface BeanFactoryAware extends Aware{
    void setBeanFactory(BeanFactory beanFactory);
}
