package site.hnfy258.bean;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {
    @Override
    public Object createBean(String beanName, BeanDefinition beanDefinition) {
        Object bean = createBeanInstance(beanDefinition);
        applyPropertyValues(beanName, bean, beanDefinition);
        initializeBean(beanName, bean, beanDefinition);
        return bean;
    }

    protected abstract Object createBeanInstance(BeanDefinition beanDefinition);
    
    protected abstract void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition);
    
    protected void initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
        applyBeanPostProcessorsBeforeInitialization(bean, beanName);
        invokeInitMethods(bean, beanName);
        applyBeanPostProcessorsAfterInitialization(bean, beanName);
    }

    private void invokeInitMethods(Object bean, String beanName) {
        if (bean instanceof InitializingBean) {
            ((InitializingBean) bean).afterPropertiesSet();
        }
    }
}