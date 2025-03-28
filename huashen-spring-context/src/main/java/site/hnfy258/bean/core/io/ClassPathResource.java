package site.hnfy258.bean.core.io;

import cn.hutool.core.lang.Assert;
import site.hnfy258.utils.ClassUtils;

import java.io.IOException;
import java.io.InputStream;

public class ClassPathResource implements Resource{
        private final String path;
        private ClassLoader classLoader;


            public ClassPathResource(String path) {
                this(path, (ClassLoader) null);
            }

            public ClassPathResource(String path, ClassLoader classLoader) {
                Assert.notNull(path, "Path must not be null");
                this.path = path;
                this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
            }

            @Override
            public InputStream getInputStream() throws IOException {
                InputStream resourceAsStream = classLoader.getResourceAsStream(path);
                if(resourceAsStream==null){
                    throw new RuntimeException("找不到资源文件"+this.path);
                }
                return resourceAsStream;
            }
}
