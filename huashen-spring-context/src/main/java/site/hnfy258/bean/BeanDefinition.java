package site.hnfy258.bean;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class BeanDefinition {

        private Class type;

        private String scope;
        private Boolean isLazy;


    public void setLazy(boolean b) {
        this.isLazy = b;
    }

    public boolean getLazy() {
        return isLazy;
    }
}
