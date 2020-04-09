package nudt.web.entity;


import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@ToString
@Entity
@Table
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sid;

    @Column
    private String serviceName;

    @Column
    private String password;

    @Column
    private String serviceDescription;


    @Column
    private String serviceIP;
}
