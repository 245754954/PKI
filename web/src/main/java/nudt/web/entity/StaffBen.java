package nudt.web.entity;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.odm.annotations.Entry;


@Data
@ToString
@Slf4j
public class StaffBen {
    //国家代码
    private String countrycode;
    //省份/州
    private String province;
    //城市
    private String city;

    private String county;

    //邮箱
    private String email;

    //组织
    private String organization;

    //隶属的业务系统
    private String serviceName;

    //单位
    private String department;

    //用户名/域名
    private String username;

    //密码
    private String password;

    //电话号码
    private String telephone;


    //地址
    private String address;




}
