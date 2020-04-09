package nudt.web.entity;


import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
//Component用于将这个Monkey bean注入到容器中

//比较麻烦的另外一种获取配置文件属性的注解方法
//@ConfigurationProperties(prefix = "monkey")
@Component
public class Monkey {

    @Value("${monkey.name}")
    private String name;
    @Value("${monkey.age}")
    private Integer age;

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

    @Override

    public String toString() {

        return "Monkey{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
