package site.hnfy258;

import site.hnfy258.annotation.PostConstruct;

public class TestBean {
    private String name;
    private int age;

    public TestBean() {
    }

    @PostConstruct
    public void init(){
        System.out.println("TestBean已加载");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {

        return "TestBean{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
