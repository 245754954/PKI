package nudt.web.repository;

import nudt.web.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission ,Integer> {

    public List<RolePermission>  findAllByRid(Integer rid);

    public RolePermission findRolePermissionByRidAndPid(Integer rid,Integer pid);


    public void deleteAllByRid(Integer roleid);
}
