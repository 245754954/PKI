package nudt.web.entity;


import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Attribute.Type;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

@Entry(objectClasses = { "inetOrgPerson", "tlsKeyInfo" })
public class User {

    @Id
    private Name dn;

    // The field used for RDN attribute myStringAttr.
    @Attribute(name = "cn")
    private String cn;

    @Attribute(name = "sn")
    private String sn;

    @Attribute(name = "mail")
    private String mail;



    @Attribute(name = "keyAlgorithm")
    private String keyAlgorithm = "RSA";

    @Attribute(name = "privateKeyFormat")
    private String privateKeyFormat = "PKCS#8";

    @Attribute(name = "publicKeyFormat")
    private String publicKeyFormat = "X.509";

    @Attribute(name = "privateKey", type = Type.BINARY)
    private byte[] privateKey;

    @Attribute(name = "publicKey", type = Type.BINARY)
    private byte[] publicKey;

    @Attribute(name = "userCertificate", type = Type.BINARY)
    private byte[] userCertificate;

    @Attribute(name = "userPKCS12", type = Type.BINARY)
    private byte[] userPKCS12;



    public Name getDn() {
        return dn;
    }

    public void setDn(Name dn) {
        this.dn = dn;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getKeyAlgorithm() {
        return keyAlgorithm;
    }

    public void setKeyAlgorithm(String keyAlgorithm) {
        this.keyAlgorithm = keyAlgorithm;
    }

    public String getPrivateKeyFormat() {
        return privateKeyFormat;
    }

    public void setPrivateKeyFormat(String privateKeyFormat) {
        this.privateKeyFormat = privateKeyFormat;
    }

    public String getPublicKeyFormat() {
        return publicKeyFormat;
    }

    public void setPublicKeyFormat(String publicKeyFormat) {
        this.publicKeyFormat = publicKeyFormat;
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(byte[] privateKey) {
        this.privateKey = privateKey;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public byte[] getUserCertificate() {
        return userCertificate;
    }

    public void setUserCertificate(byte[] userCertificate) {
        this.userCertificate = userCertificate;
    }

    public byte[] getUserPKCS12() {
        return userPKCS12;
    }

    public void setUserPKCS12(byte[] userPKCS12) {
        this.userPKCS12 = userPKCS12;
    }



    @Override
    public String toString(){
       /*StringBuilder privateKeyString =new StringBuilder();
       if (privateKey!=null) {
           for (byte b:privateKey) {
               privateKeyString.append(Byte.toString(b));
           }
       }
       StringBuilder publicKeyString =new StringBuilder();
       if (publicKey!=null) {
           for (byte b:publicKey) {
               publicKeyString.append(Byte.toString(b));
           }
       }
       StringBuilder userCertificateString =new StringBuilder();
       if (userCertificate!=null) {
           for (byte b:userCertificate) {
               userCertificateString.append(Byte.toString(b));
           }
       }

       StringBuilder userPKCS12String =new StringBuilder();
       if (userPKCS12!=null) {
           for (byte b:userPKCS12) {
               userPKCS12String.append(Byte.toString(b));
           }
       }*/

        return String.format(
                "dn=%1$s \ncn=%2$s \nsn=%3$s \nmail=%4$s \nkeyAlgorithm=%5$s \nprivateKeyFormat=%6$s \nprivateKey=%7$s \npublicKeyFormat=%8$s \npublicKey=%9$s \nuserCertificate=%10$s \nuserPKCS12=%11$s",
                dn.toString(), cn, sn, mail, keyAlgorithm, privateKeyFormat, privateKey==null?"":new String(privateKey),publicKeyFormat, publicKey==null?"":new String(publicKey), userCertificate==null?"":new String(userCertificate),userPKCS12==null?"":new String(userPKCS12));

    }
}
