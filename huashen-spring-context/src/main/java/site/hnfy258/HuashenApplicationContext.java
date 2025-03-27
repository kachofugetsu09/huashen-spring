package site.hnfy258;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HuashenApplicationContext {
    private Map<String , BeanDefinition> beanDefinitionMap = new HashMap<>();
    private Map<String , Object> singletonObjects = new HashMap<>(); //单例池
        public HuashenApplicationContext(Class configClass) {
            //扫描Bean
            scan(configClass);

            //非懒加载的单例Bean
            for(Map.Entry<String, BeanDefinition> beanDefinitionEntry : beanDefinitionMap.entrySet()){
                String beanName = beanDefinitionEntry.getKey();
                BeanDefinition beanDefinition = beanDefinitionEntry.getValue();
                if(beanDefinition.getScope().equals("singleton")&&
                !beanDefinition.getLazy()){
                    Object bean = createBean(beanName, beanDefinition);
                    singletonObjects.put(beanName,bean);
                }
            }
        }

    private void scan(Class configClass) {
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
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
                                beanDefinitionMap.put(beanName, beanDefinition);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("扫描类时出错", e);
            }
        }
    }

    public Object createBean(String beanName,BeanDefinition beanDefinition){
           Class type = beanDefinition.getType();
        try {
            Object instance = type.newInstance();
            return instance;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getBean(String beanName){

            if(!beanDefinitionMap.containsKey(beanName)){
                throw new IllegalArgumentException("beanName is not found");
            }

            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if(beanDefinition.getScope().equals("prototype")){
                return createBean(beanName,beanDefinition);
            }
            else{
                Object singletonBean = singletonObjects.get(beanName);
                if(singletonBean == null){
                    singletonBean = createBean(beanName,beanDefinition);
                    singletonObjects.put(beanName,singletonBean);
                }
                return singletonBean;
            }
        }
    }
