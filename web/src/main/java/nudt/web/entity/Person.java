package nudt.web.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

//将yml文件中配置的属性值映射到这个bean中
//我们需要使用到一个注解@ConfigurationProperties，这个注解的作用就是告诉springboot，这个对象
//里边属性的值都是来自于配置文件,这个prefix代表我们从配置文件里边的person开始逐步向下映射
//映射完毕以后我们还需要将这个bean添加到容器中，我们还需要使用一个主节，@component
@Component//用于将这个bean注入到容器
//从全局配置文件中找前缀为person的属性，依次注入本对象，如果属性值不在全局配置文件中，则需要
@ConfigurationProperties(prefix = "person")

public class Person {

    private String lastName;
    private Integer age;
    private Boolean boss;
    private Date birth;

    private HashMap<String,Object> maps;

    private List<Object> lists;
    private Dog dog;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getBoss() {
        return boss;
    }

    public void setBoss(Boolean boss) {
        this.boss = boss;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public HashMap<String, Object> getMaps() {
        return maps;
    }

    public void setMaps(HashMap<String, Object> maps) {
        this.maps = maps;
    }

    public List<Object> getLists() {
        return lists;
    }

    public void setLists(List<Object> lists) {
        this.lists = lists;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    @Override
    public String toString() {
        return "Person{" +
                "lastName='" + lastName + '\'' +
                ", age=" + age +
                ", boss=" + boss +
                ", birth=" + birth +
                ", maps=" + maps +
                ", lists=" + lists +
                ", dog=" + dog +
                '}';
    }
}
