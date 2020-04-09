package nudt.web.entity;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table
public class ServiceRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //角色的id
    @Column
    private Integer rid;

    //业务系统的id
    @Column
    private Integer sid;
}
