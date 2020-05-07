package nudt.web.entity;


import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Table
@Entity
@ToString
public class ServicePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer serviceId;

    @Column
    private  Integer permissionId;

}
