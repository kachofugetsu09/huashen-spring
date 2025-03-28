package site.hnfy258.bean.config;

import lombok.Getter;
import lombok.Setter;

public class BeanDefinition {


    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setLazy(Boolean lazy) {
        isLazy = lazy;
    }

    private Class type;

        private String scope;
        private Boolean isLazy;

    public BeanDefinition(Class<?> clazz) {
        this.type = clazz;
        this.scope = "singleton";
        this.isLazy = false;
    }


    public void setLazy(boolean b) {
        this.isLazy = b;
    }

    public boolean getLazy() {
        return isLazy;
    }
}
