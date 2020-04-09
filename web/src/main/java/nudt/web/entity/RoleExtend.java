package nudt.web.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Data
@ToString
public class RoleExtend {



    private Integer roleid;

    private String rolename;


    private String roledescription;

    private  String serviceName;

    private String serviceID;

}
