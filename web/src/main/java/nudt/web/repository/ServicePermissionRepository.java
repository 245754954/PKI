package nudt.web.repository;

import nudt.web.entity.ServicePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicePermissionRepository extends JpaRepository<ServicePermission ,Integer> {

        public List<ServicePermission>  findServicePermissionsByServiceIdIn(List<Integer> ids);


        public void deleteByPermissionId(Integer pid);
}
