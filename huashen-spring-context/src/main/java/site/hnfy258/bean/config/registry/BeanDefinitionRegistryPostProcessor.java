package site.hnfy258.bean.config.registry;

import site.hnfy258.bean.config.BeanPostProcessor;

public interface BeanDefinitionRegistryPostProcessor extends BeanPostProcessor {
    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws Exception;
}