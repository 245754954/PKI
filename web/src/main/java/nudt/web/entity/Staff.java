package nudt.web.entity;


import lombok.Data;
import lombok.ToString;


import javax.naming.Name;
import javax.persistence.*;

//采用JPA，我们配置对象关系映射，因此需要使用JPA的注解
@Entity//告诉JPA这是一个实体类（也就是和数据库对应的一个类，不是一个普通的Java bean）
@Table//这个类和数据库tab_user数据表相关，使用@Table指定该实体类和哪个数据表对应，如果省略，那么数据库表的名字就是实体类
@Data
@ToString
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //国家代码
    @Column
    private String countrycode;
    //省份/州
    @Column
    private String province;
    //城市
    @Column
    private String city;

    //邮箱
    @Column
    private String email;

    //组织
    @Column
    private String organization;

    //单位
    @Column
    private String department;

    //用户名/域名
    @Column
    private String username;

    @Column
    private String surname;
    //密码
    @Column
    private String password;

    //电话号码
    @Column
    private String telephone;

    //地址
    @Column
    private String address;

    @Column
    private String description;
    //是否激活
    @Column
    private Integer code;

}
