package site.hnfy258.bean.core.io;

import cn.hutool.core.lang.Assert;

import java.net.MalformedURLException;
import java.net.URL;

public class DefaultResourceLoader  implements ResourceLoader{
    @Override
    public Resource getResource(String location) {
        Assert.notNull(location, "location 不能为空");
        if(location.startsWith(CLASSPATH_URL_PREFIX)){
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));
        }
        else{
            try{
                URL url = new URL(location);
                return new UrlResource(url);
            } catch (MalformedURLException e) {
                return  new FileSystemResource(location);
            }
        }
    }
}
