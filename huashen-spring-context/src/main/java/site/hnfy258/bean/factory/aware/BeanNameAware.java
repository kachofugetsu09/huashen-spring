package site.hnfy258.bean.factory.aware;

public interface BeanNameAware extends Aware {

    void setBeanName(String name);

}