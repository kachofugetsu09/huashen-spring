package site.hnfy258.bean.factory.aware;

import site.hnfy258.bean.ApplicationContext;
import site.hnfy258.exception.BeansException;

public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;

}