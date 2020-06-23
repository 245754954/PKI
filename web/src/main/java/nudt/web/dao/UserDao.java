package nudt.web.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.naming.Name;
import javax.naming.directory.SearchControls;

import nudt.web.entity.User;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Repository;


@Repository(value="userDao")
public class UserDao {
    @Autowired
    private LdapTemplate ldapTemplate;

    public void setLdapTemplate(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public void create(User user) {
        DirContextAdapter context = new DirContextAdapter(buildDn(user));
        mapToContextForCreate(user, context);
        ldapTemplate.bind(context);
    }

    public void update(User user) throws Exception {
        Name dn = buildDn(user);
        DirContextOperations context = ldapTemplate.lookupContext(dn);
        if (context == null) {
            throw new Exception("Entry " + dn.toString() + " does not exist");
        }
        mapToContextForUpdate(user, context);
        if (context != null) {
            ldapTemplate.modifyAttributes(context);
        }

    }

    public void rename(Name oldDn, Name newDn) {
        ldapTemplate.rename(oldDn, newDn);
    }

    public void delete(User user) {
        ldapTemplate.unbind(buildDn(user), true);
    }

    @SuppressWarnings("unchecked")
    public List<User> findAll() {
        EqualsFilter filter = new EqualsFilter("objectclass", "inetOrgPerson");
        System.out.println(filter.toString());
        return ldapTemplate.search(DistinguishedName.EMPTY_PATH,
                filter.encode(), getContextMapper());
    }

    @SuppressWarnings("unchecked")
    public List<User> findByDN(String dn, String filter) {

        if (StringUtils.isEmpty(filter)) {
            filter = new EqualsFilter("objectclass", "inetOrgPerson").encode();
        }
        return ldapTemplate.search(dn, filter, SearchControls.SUBTREE_SCOPE,
                getContextMapper());
    }

    protected ContextMapper getContextMapper() {
        return new PersonContextMapper();
    }

    protected Name buildDn(User user) {
        return user.getDn();
    }

    protected Name buildDn(String dn) {
        DistinguishedName dName = new DistinguishedName(dn);

        return dName;
    }

    protected void mapToContextForUpdate(User user, DirContextOperations context) {

        boolean update = false;

        if (StringUtils.isNotEmpty(user.getCn())) {
            context.setAttributeValue("cn", user.getCn());
            context.setAttributeValue("sn", user.getCn());
            context.setAttributeValue("mail", user.getMail());
            update = true;
        }

        if (user.getPrivateKey() != null) {
            context.setAttributeValue("privateKey", user.getPrivateKey());
            update = true;
        }
        if (user.getPublicKey() != null) {
            context.setAttributeValue("publicKey", user.getPublicKey());
            context.setAttributeValue("userCertificate",
                    user.getUserCertificate());
            update = true;
        }
        if (user.getUserPKCS12() != null) {
            context.setAttributeValue("userPKCS12", user.getUserPKCS12());
            update = true;
        }

        if (update) {
            context.setAttributeValues("objectclass", new String[] { "top",
                    "tlsKeyInfo", "inetOrgPerson" });

        } else {
            context = null;
        }

    }

    protected void mapToContextForCreate(User user, DirContextOperations context) {

        context.setAttributeValues("objectclass", new String[] { "top",
                "tlsKeyInfo", "inetOrgPerson" });
        context.setAttributeValue("cn", user.getCn());
        context.setAttributeValue("sn", user.getSn());
        context.setAttributeValue("mail", user.getMail());
        context.setAttributeValue("keyAlgorithm",user.getKeyAlgorithm());
        context.setAttributeValue("privateKeyFormat", user.getPrivateKeyFormat());
        context.setAttributeValue("publicKeyFormat", user.getPublicKeyFormat());
        context.setAttributeValue("privateKey", user.getPrivateKey());
        context.setAttributeValue("publicKey", user.getPublicKey());
        context.setAttributeValue("userCertificate", user.getUserCertificate());
        context.setAttributeValue("userPKCS12", user.getUserPKCS12());

    }

    private static class PersonContextMapper extends AbstractContextMapper {
        public Object doMapFromContext(DirContextOperations context) {
            User user = new User();
            user.setDn(new DistinguishedName(context.getDn()));
            user.setCn(context.getStringAttribute("cn"));
            user.setSn(context.getStringAttribute("sn"));
            user.setMail(context.getStringAttribute("mail"));
            user.setKeyAlgorithm(context.getStringAttribute("keyAlgorithm"));



            user.setPrivateKeyFormat(context
                    .getStringAttribute("privateKeyFormat"));


            if(StringUtils.isNotEmpty(context.getStringAttribute("privateKey"))){
                user.setPrivateKey(context.getStringAttribute("privateKey")
                        .getBytes());
            }

            user.setPublicKeyFormat(context
                    .getStringAttribute("publicKeyFormat"));
            if(StringUtils.isNotEmpty(context.getStringAttribute("publicKey"))){
                user.setPublicKey(context.getStringAttribute("publicKey")
                        .getBytes());
            }


            user.setUserCertificate((byte[]) context
                    .getObjectAttribute("userCertificate"));

            if(StringUtils.isNotEmpty(context.getStringAttribute("userPKCS12"))){
                user.setUserPKCS12(context.getStringAttribute("userPKCS12")
                        .getBytes());
            }



            return user;
        }
    }
}