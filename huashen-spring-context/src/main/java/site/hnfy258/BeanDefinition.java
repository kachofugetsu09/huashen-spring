package site.hnfy258;

public class BeanDefinition {

        private Class type;
        private String scope;
        private Boolean isLazy;

            public void setType(Class type) {
                this.type = type;
            }

            public void setScope(String scope) {
                this.scope = scope;
            }

            public void setLazy(Boolean lazy) {
                isLazy = lazy;
            }

            public Boolean getLazy() {
                return isLazy;
            }

            public String getScope() {
                return scope;
            }

            public Class getType() {
                return type;
            }


}
