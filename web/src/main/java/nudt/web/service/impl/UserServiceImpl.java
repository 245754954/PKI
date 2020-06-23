package nudt.web.service.impl;

import nudt.web.dao.UserDao;
import nudt.web.entity.User;
import nudt.web.service.UserService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NamingException;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;


    @Override
    public void create(Name dn,String cn, String email,String sn,String algorithm,byte[] p12Cert, byte[] publicKey, byte[] privateKey,byte[] cert) throws InvalidNameException, InvalidNameException {

        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("Start CREATE record...");
        long startTime = System.currentTimeMillis();
        //byte[] encodedPrivateCert = Base64.encodeBase64(privateKey);
        //byte[] encodedP12Cert = Base64.encodeBase64(p12Cert);

         dn.add("cn=" + cn);

        User user = new User();
        user.setDn(dn);
        user.setCn(cn);
        user.setSn(sn);
        user.setMail(email);
        user.setKeyAlgorithm(algorithm);
        //user.setPublicKeyFormat("X.509");
        //user.setPrivateKeyFormat("PKCS#12");
        user.setPublicKeyFormat("PKCS#12");
        user.setPrivateKeyFormat("PKCS#12");

        user.setPrivateKey(privateKey);
        user.setPublicKey(publicKey);
        user.setUserCertificate(cert);
        user.setUserPKCS12(cert);

        userDao.create(user);

        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;

        System.out.println("CREATE Time Cost(ms): " + duration);
    }



    public List<User> search(String dn, String filter)
            throws NamingException {

        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("Start SEARCH record...");
        long startTime = System.currentTimeMillis();

        List<User> result = userDao.findByDN(dn, filter);

        System.out.print("SEARCH Result count: ");
        System.out.println(result == null ? 0 : result.size());

        for (User user : result) {
            System.out.println(user.toString());
        }

        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;

        System.out.println("SEARCH Time Cost(ms): " + duration);
        return result;
    }

    public void update(Name oldDName, String oldCn, Name newDName,
                       String newCn, byte[] p12Entry, byte[] publicKey, byte[] privateKey,byte[]cert)
            throws Exception {

        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("Start UPDATE record...");

        long startTime = System.currentTimeMillis();



        userDao.rename(oldDName, newDName);
        System.out.println("Entry rename to" + newDName.toString());

        User user = new User();

        user.setDn(newDName);

        user.setCn(newCn);
        user.setMail(newCn);
        user.setSn(newCn);
        if (p12Entry != null) {
            user.setUserPKCS12(p12Entry);
        }
        if (publicKey != null) {
            user.setPublicKey(publicKey);
            user.setUserCertificate(publicKey);
        }
        if (privateKey != null) {
            user.setPrivateKey(privateKey);
        }

        if(cert!=null)
        {
            user.setUserCertificate(cert);
        }
        userDao.update(user);

        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;

        System.out.println("UPDATE Time Cost(ms): " + duration);
        // System.exit(result);

    }

    public void delete(Name dn) {

        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("Start DELETE record...");
        long startTime = System.currentTimeMillis();


        User user = new User();
        user.setDn(dn);
        userDao.delete(user);

        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;

        System.out.println("DELETE Time Cost(ms): " + duration);
    }



    public byte[] readDataFromFile(String path) {

        InputStream is = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            is = new BufferedInputStream(new FileInputStream(path));
            byte[] b = new byte[1024];
            int n;
            while ((n = is.read(b)) != -1) {
                out.write(b, 0, n);
            }

        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return out.toByteArray();
    }
}
