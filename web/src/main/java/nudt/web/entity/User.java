package nudt.web.entity;


import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

//采用JPA，我们配置对象关系映射，因此需要使用JPA的注解
@Entity//告诉JPA这是一个实体类（也就是和数据库对应的一个类，不是一个普通的Java bean）
@Table(name="user")//这个类和数据库tab_user数据表相关，使用@Table指定该实体类和哪个数据表对应，如果省略，那么数据库表的名字就是实体类
@Data
@ToString
public class User {

    @Id//这是一个主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)// 自增长主键
    private Integer id;
    @Column(name = "username",length = 255)//这是和数据库表的某个列对应,数据库中这一列的名字叫做last_name
    private String username;
    @Column//省略那么的话，那么数据库列名字就是和字段名字一样
    private String password;


}
