package nudt.web.repository;

import nudt.web.entity.Cert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertRepository extends JpaRepository<Cert,String> {
    //根据证书的序列号，查找与证书相关的信息
    public  Cert findCertBySerialNumber(String serialNumber);

    //根据用户的名字或者个体的名字来查找证书的相关信息
    public  Cert findCertByUsername(String username);

    //根据数字证书的序列号，删除数据库的数字证书记录
    public void deleteBySerialNumber(String serialNumber);


}
