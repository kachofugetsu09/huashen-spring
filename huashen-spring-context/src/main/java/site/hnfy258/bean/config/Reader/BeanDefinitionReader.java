package site.hnfy258.bean.config.Reader;

import lombok.Getter;
import lombok.Setter;
import site.hnfy258.bean.core.io.Resource;
import site.hnfy258.bean.core.io.ResourceLoader;
import site.hnfy258.bean.factory.BeanFactory;

public interface BeanDefinitionReader {
    BeanFactory beanFactory();
    ResourceLoader resourceLoader();
    void loadBeanDefinitions(Resource resource);
    void loadBeanDefinitions(String location);
    void loadBeanDefinitions(Resource... resouces);
}
