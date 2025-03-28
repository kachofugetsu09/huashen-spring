package site.hnfy258;

import site.hnfy258.annotation.Component;
import site.hnfy258.annotation.ComponentScan;
import site.hnfy258.annotation.Scope;
import site.hnfy258.bean.config.BeanDefinition;
import site.hnfy258.bean.config.BeanPostProcessor;
import site.hnfy258.bean.config.InstantiationStrategy.DefaultListableBeanFactory;

import java.io.File;
import java.net.URL;
import java.util.Map;

public class HuashenApplicationContext {
    private DefaultListableBeanFactory beanFactory;

    public HuashenApplicationContext(Class<?> configClass) {
        this.beanFactory = new DefaultListableBeanFactory();
        beanFactory.useSmartInstantiationStrategy();
        scan(configClass);
        registerBeanPostProcessors();
        finishBeanFactoryInitialization();
    }


    private void scan(Class configClass) {
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            System.out.println(configClass.getName());
            ComponentScan componentScan = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
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
                                BeanDefinition beanDefinition = new BeanDefinition();
                                beanDefinition.setType(clazz);
                                beanDefinition.setLazy(false);

                                if (clazz.isAnnotationPresent(Scope.class)) {
                                    beanDefinition.setScope(clazz.getAnnotation(Scope.class).value());
                                } else {
                                    beanDefinition.setScope("singleton");
                                }

                                String beanName = clazz.getAnnotation(Component.class).value();
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

    private void registerBeanPostProcessors() {
        for (Map.Entry<String, BeanDefinition> entry : beanFactory.getBeanDefinitionMap().entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            if (BeanPostProcessor.class.isAssignableFrom(beanDefinition.getType())) {
                BeanPostProcessor beanPostProcessor = (BeanPostProcessor) beanFactory.getBean(beanName);
                beanFactory.addBeanPostProcessor(beanPostProcessor);
            }
        }
    }

    private void finishBeanFactoryInitialization() {
        for (Map.Entry<String, BeanDefinition> entry : beanFactory.getBeanDefinitionMap().entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            if (beanDefinition.getScope().equals("singleton") && !beanDefinition.getLazy()) {
                beanFactory.getBean(beanName);
            }
        }
    }

    public Object getBean(String name) {
        return beanFactory.getBean(name);
    }
}
