package nudt.web.repository;

import nudt.web.entity.ServiceRole;
import nudt.web.service.RoleService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRoleRepository extends JpaRepository<ServiceRole,Integer> {

    public List<ServiceRole> findAllBySid(Integer sid);


    public ServiceRole findServiceRoleByRid(Integer rid);

    public void deleteByRid(Integer rid);


    public List<ServiceRole> findServiceRolesByRid(Integer roleid);

}
