package nudt.web.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.ldap.odm.annotations.*;

import javax.naming.Name;
//@Id标注的是这个实体的DN，@Attribute表明这是objectClass的一个属性，@Dnattribute标注的属性都是属于自动构建DN时的一部分
@Data
@ToString
@Entry(base = "ou=taobao",objectClasses = { "person",  "top" })
public class Clerk {
    @Id
    private Name dn;

    @Attribute(name="cn")
    @DnAttribute(value="cn",index=1)
    private String fullName;

    @Attribute(name="sn")
    private String lastName;


    @Attribute(name="userPassword")
    private String userPassword;

    @Attribute(name="telephoneNumber")
    private String telephoneNumber;

    @Attribute(name="description")
    private String description;
}
