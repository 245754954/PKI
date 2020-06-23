package com.taoyuanx.test;


import nudt.core.exception.CertException;
import nudt.core.openssl.cert.CertUtil;
import nudt.web.AppServerApplication;
import nudt.web.entity.User;
import nudt.web.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NamingException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplication.class)
public class TestUser {



    @Autowired
    UserService userService;

    @Test
    public void save() throws CertException, CertificateEncodingException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, InvalidNameException {

        Name dn = LdapNameBuilder.newInstance()
                .add("o","CA")
                .add("ou", "NUDT")
                .build();
        String cn = "zfc";
        String sn = "Z";
        String email = "245754954@qq.com";
        String algorithm="RSA";
        String signatureAlgorithm = "SHA256";
        byte[] publicKey = CertUtil.readPublicKeyPem ("E:\\client\\cert\\rsa\\client\\rsa_client_pub.pem").getEncoded();
        byte[] privateKey = CertUtil.readPrivateKeyPem("E:\\client\\cert\\rsa\\client\\rsa_client_pri.pem").getEncoded();
        byte[] p12 = CertUtil.readKeyStore("E:\\client\\cert\\rsa\\client\\rsa_client.p12","123456").getKey("rsa_client","123456".toCharArray()).getEncoded();
        byte[] cert = CertUtil.readX509Cert("E:\\client\\cert\\rsa\\client\\rsa_client_base64.cer").getEncoded();

        userService.create(dn,cn,email,sn,algorithm,p12,publicKey,privateKey,cert);
    }

    @Test
    public void search() throws CertException, CertificateEncodingException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, NamingException {

        Name dn = LdapNameBuilder.newInstance()
                .add("o","CA")
                .add("ou", "NUDT")
                .build();
        String cn = "zfc";

       dn.add("cn="+cn);
       userService.search(dn.toString(),null);
    }


    @Test
    public void update() throws Exception {


        //为待添加的数据构建dn
//        Name dn = LdapNameBuilder.newInstance()
//                .add("o", "CA")
//                .add("ou",staffBen.getOrganization())
//                .build();
//        AndFilter filter = new AndFilter();
//        filter.and(new EqualsFilter("objectClass", "person"));
//        filter.and(new EqualsFilter("cn",staffBen.getUsername()));
//        List<User> users = userService.search(dn.toString(),filter.encode());
//        if(users==null||users.size()==0)
//        {
//            userService.create(dn,staffBen.getUsername(),staffBen.getEmail(),staffBen.getUsername(),null,null,null,null,null);
//        }




        Name oldDn = LdapNameBuilder.newInstance()
                .add("o","CA")
                .add("ou", "NUDT")
                .build();
        String oldCn = "zfc";
        oldDn.add("cn="+oldCn);

        Name newDn = LdapNameBuilder.newInstance()
                .add("o","CA")
                .add("ou", "NUDT")
                .build();
        String newCn = "zfx";
        newDn.add("cn="+newCn);

        userService.update(oldDn,oldCn,newDn,newCn,null,null,null,null);
    }

    @Test
    public void delete() throws Exception {

        Name dn = LdapNameBuilder.newInstance()
                .add("o","CA")
                .add("ou", "NUDT")
                .build();
        String cn = "zfc";
        dn.add("cn="+cn);



        userService.delete(dn);
    }


}
