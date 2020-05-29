package nudt.web.entity;


import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

//采用JPA，我们配置对象关系映射，因此需要使用JPA的注解
@Entity//告诉JPA这是一个实体类（也就是和数据库对应的一个类，不是一个普通的Java bean）
@Table//这个类和数据库tab_user数据表相关，使用@Table指定该实体类和哪个数据表对应，如果省略，那么数据库表的名字就是实体类
@Data
@ToString
public class Cert {


    //证书编号
    @Id
    private String  serialNumber;

    //用户名
    @Column
    private String username;

    @Column
    private Integer version;

    @Column
    private String issuerDN;

    @Column
    private String signatureAlgorithm;

    @Column
    private String algorithm;


    //密钥长度
    @Column
    private Integer keysize;

    //证书类型
    @Column
    private Integer caType;

    //签名算法类型
    @Column
    private String sigAlg;
    //证书保存路径
    @Column
    private String certDir;

    //公钥保存路径
    @Column
    private String pub_pem_path;

    //私钥保存路径
    @Column
    private String pri_pem_path;

    @Column
    private String signatureValue;


    //p12密钥库文件保存路径
    @Column
    private String p12_path;
    //用户别名
    @Column
    private String userAlias;

    //密钥库密码
    @Column
    private String password;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Column
    private Date notBefore;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Column
    private Date notAfter;

    //证书的状态,有效，冻结，还是过期
    @Column
    private String status;

    //将来加密证书的文件夹
    @Column
    private String encryptpassword;



}
