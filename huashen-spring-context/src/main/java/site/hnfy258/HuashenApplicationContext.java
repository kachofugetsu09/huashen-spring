package site.hnfy258;

import site.hnfy258.annotation.Component;
import site.hnfy258.annotation.ComponentScan;
import site.hnfy258.annotation.Scope;
import site.hnfy258.bean.ApplicationContext;
import site.hnfy258.bean.config.BeanDefinition;
import site.hnfy258.bean.config.BeanPostProcessor;
import site.hnfy258.bean.factory.DefaultListableBeanFactory;
import site.hnfy258.bean.config.Reader.XmlBeanDefinitionReader;
import site.hnfy258.bean.factory.aware.ApplicationContextAware;
import site.hnfy258.exception.BeansException;

import java.io.File;
import java.net.URL;
import java.util.Map;

public class HuashenApplicationContext implements ApplicationContext {
    private DefaultListableBeanFactory beanFactory;
    private XmlBeanDefinitionReader xmlBeanDefinitionReader;


    public HuashenApplicationContext(Class<?> configClass) {
        this.beanFactory = new DefaultListableBeanFactory();
        beanFactory.useSmartInstantiationStrategy();
        scan(configClass);
        registerBeanPostProcessors();
        finishBeanFactoryInitialization();
    }

    public HuashenApplicationContext(String xmlPath) {
        this.beanFactory = new DefaultListableBeanFactory();
        beanFactory.useSmartInstantiationStrategy();
        xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        xmlBeanDefinitionReader.loadBeanDefinitions(xmlPath);
        registerBeanPostProcessors();
        finishBeanFactoryInitialization();
    }


    private void scan(Class<?> configClass) {
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScan = configClass.getAnnotation(ComponentScan.class);
            String packagePath = componentScan.value();
            String path = packagePath.replace(".", "/");

            ClassLoader classLoader = this.getClass().getClassLoader();
            URL resource = classLoader.getResource(path);
            if (resource == null) {
                throw new RuntimeException("扫描路径不存在: " + path);
            }

            try {
                File file = new File(resource.toURI());
                if (file.isDirectory()) {
                    for (File classFile : file.listFiles()) {
                        if (classFile.getName().endsWith(".class")) {
                            String absolutePath = classFile.getAbsolutePath();
                            String className = absolutePath
                                    .substring(absolutePath.indexOf("site"))
                                    .replace(".class", "")
                                    .replace(File.separator, ".");

                            Class<?> clazz = classLoader.loadClass(className);
                            if (clazz.isAnnotationPresent(Component.class)) {
                                BeanDefinition beanDefinition = getBeanDefinition(clazz);

                                Component component = clazz.getAnnotation(Component.class);
                                String beanName = component.value().isEmpty() ? clazz.getSimpleName() : component.value();
                                beanFactory.registerBeanDefinition(beanName, beanDefinition);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("扫描类时出错", e);
            }
        }
    }

    private static BeanDefinition getBeanDefinition(Class<?> clazz) {
        BeanDefinition beanDefinition = new BeanDefinition(clazz);
        beanDefinition.setLazy(false);

        if (clazz.isAnnotationPresent(Scope.class)) {
            beanDefinition.setScope(clazz.getAnnotation(Scope.class).value());
        } else {
            beanDefinition.setScope("singleton");
        }
        return beanDefinition;
    }

    private void registerBeanPostProcessors() {
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            if (BeanPostProcessor.class.isAssignableFrom(beanDefinition.getType())) {
                BeanPostProcessor beanPostProcessor = (BeanPostProcessor) beanFactory.getBean(beanName);
                beanFactory.addBeanPostProcessor(beanPostProcessor);
            }
        }
    }

    private void finishBeanFactoryInitialization() {
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            if (beanDefinition.getScope().equals("singleton") && !beanDefinition.getLazy()) {
                beanFactory.getBean(beanName);
            }
        }
    }

    @Override
    public Object createBean(String beanName, BeanDefinition beanDefinition) {
        return beanFactory.createBean(beanName, beanDefinition);
    }

    @Override
    public Object getBean(String name) throws Exception {
        Object bean = beanFactory.getBean(name);
        if (bean instanceof ApplicationContextAware) {
            ((ApplicationContextAware) bean).setApplicationContext(this);
        }
        return bean;
    }


    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return beanFactory.getBeansOfType(type);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanFactory.getBeanDefinitionNames();
    }

}