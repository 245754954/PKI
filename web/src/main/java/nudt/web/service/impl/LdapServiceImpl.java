package nudt.web.service.impl;

import nudt.web.service.LdapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;

public class LdapServiceImpl implements LdapService {

    @Autowired
    LdapTemplate ldapTemplate;

    @Override
    public void queryLdap() {

       return ;

    }
}
