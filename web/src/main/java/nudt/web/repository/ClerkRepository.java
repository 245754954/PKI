package nudt.web.repository;

import nudt.web.entity.Clerk;
import org.springframework.data.ldap.repository.LdapRepository;

import javax.naming.Name;

public interface ClerkRepository extends LdapRepository<Clerk>{

    
}
