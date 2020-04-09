package nudt.web.entity;


import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@ToString
@Table
@Entity
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer pid;

    @Column
    private Integer rid;
}
