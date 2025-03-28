package site.hnfy258.bean;

public interface BeanFactory {
    Object createBean(String beanName, BeanDefinition beanDefinition);
}