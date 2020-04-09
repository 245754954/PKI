package nudt.web.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;


@Data
@Entity
@ToString
@Table
public class StaffRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer sid;

    @Column
    private Integer rid;
}
