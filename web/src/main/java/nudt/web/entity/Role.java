package nudt.web.entity;


import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Table
@ToString
@Entity
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column
	private String name;

	@Column
	private String description;





	
}
