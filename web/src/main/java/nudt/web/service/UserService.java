package nudt.web.service;

import nudt.web.dao.UserDao;
import nudt.web.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NamingException;
import java.util.List;

public interface UserService {





    public void create(Name dn,String cn, String email,String sn,String algorithm,byte[] p12Cert, byte[] publicKey, byte[] privateKey,byte[] cert) throws InvalidNameException;

    public List<User> search(String dn, String filter) throws NamingException;





    public void update(Name oldDName, String oldCn, Name newDName,
                       String newCn, byte[] p12Entry, byte[] publicKey, byte[] privateKey,byte[]cert) throws Exception;


    public void delete(Name dn);



    public byte[] readDataFromFile(String path);
}
