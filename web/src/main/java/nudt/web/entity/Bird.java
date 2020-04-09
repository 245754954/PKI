package nudt.web.entity;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
//#这个配置文件的作用是用来演示，bean如何加载非全局配置文件里边的属性值
//        #读取非全局配置文件的属性值，我们需要使用三个注解，第一个PropertySource用于指定当前bean的赋值采用
//        #哪个配置文件，第二个@ConfigurationProperties，用于指定属性值映射的方式
//        #@component用于将当前bena加载到容器里边
@PropertySource(value = {"classpath:bird.properties"})//用于加载指定的配置文件
@Component//将bean注入到容器
@ConfigurationProperties(prefix = "bird")//属性映射匹配规则
public class Bird {

    private String name;
    private Integer age;
    private Boolean boss;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Bird{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", boss=" + boss +
                '}';
    }
}
