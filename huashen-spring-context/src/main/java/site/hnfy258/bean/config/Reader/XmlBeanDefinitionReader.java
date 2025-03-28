package site.hnfy258.bean.config.Reader;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import site.hnfy258.bean.config.BeanDefinition;
import site.hnfy258.bean.config.InstantiationStrategy.DefaultListableBeanFactory;
import site.hnfy258.bean.core.io.DefaultResourceLoader;
import site.hnfy258.bean.core.io.Resource;
import site.hnfy258.bean.core.io.ResourceLoader;
import site.hnfy258.bean.factory.BeanFactory;

import java.io.InputStream;

@Setter
@Getter
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {
    private ResourceLoader resourceLoader;
    private BeanFactory beanFactory;

    public XmlBeanDefinitionReader(BeanFactory beanFactory) {
        super(beanFactory);
        this.beanFactory = beanFactory;
        this.resourceLoader = new DefaultResourceLoader();

    }

    public XmlBeanDefinitionReader(BeanFactory beanFactory, ResourceLoader resourceLoader) {
        super(beanFactory, resourceLoader);
        this.beanFactory = beanFactory;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void loadBeanDefinitions(Resource resource) {
        try {
            try (InputStream inputStream = resource.getInputStream()) {
                doLoadBeanDefinitions(inputStream);
            }
        } catch (Exception e) {
            throw new RuntimeException("XML文件加载失败", e);
        }
    }

    private void doLoadBeanDefinitions(InputStream inputStream) throws ClassNotFoundException {
        Document doc = XmlUtil.readXML(inputStream);
        Element root = doc.getDocumentElement();
        NodeList childNodes = root.getChildNodes();

        for (int i = 0; i < childNodes.getLength(); i++) {
            if (!(childNodes.item(i) instanceof Element)) {
                continue;
            }
            if (!childNodes.item(i).getNodeName().equals("bean")) {
                continue;
            }

            Element bean = (Element) childNodes.item(i);
            String id = bean.getAttribute("id");
            String name = bean.getAttribute("name");
            String className = bean.getAttribute("class");
            String scope = bean.getAttribute("scope");
            String lazyInit = bean.getAttribute("lazy-init");

            Class<?> clazz = Class.forName(className);

            String beanName = StrUtil.isNotEmpty(id) ? id : name;
            if (StrUtil.isEmpty(beanName)) {
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }

            BeanDefinition beanDefinition = new BeanDefinition(clazz);

            if (StrUtil.isNotEmpty(scope)) {
                beanDefinition.setScope(scope);
            }

            if (StrUtil.isNotEmpty(lazyInit)) {
                beanDefinition.setLazy(Boolean.parseBoolean(lazyInit));
            }

            if (beanFactory instanceof DefaultListableBeanFactory) {
                DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
                defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinition);
            } else {
                throw new RuntimeException("BeanFactory必须是DefaultListableBeanFactory的实例");
            }
        }
    }

    @Override
    public void loadBeanDefinitions(String location) {
        Resource resource = this.resourceLoader.getResource(location);
        loadBeanDefinitions(resource);
    }

    @Override
    public void loadBeanDefinitions(Resource... resources) {
        for (Resource resource : resources) {
            loadBeanDefinitions(resource);
        }
    }
}
