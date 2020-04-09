package nudt.web.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;


@Data
@Entity
@ToString
public class CRL {

    //证书的序列号
    @Id
    private String serialNumber;

     @Column(length = 5000)
    private String pubKey;

    @Column(length = 5000)
    private String priKey;

    @Column
    private Boolean isValidate;

    @Column
    private Date createTime;


}
