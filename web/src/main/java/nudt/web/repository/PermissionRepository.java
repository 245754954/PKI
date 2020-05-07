package nudt.web.repository;


import nudt.web.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


public interface PermissionRepository extends JpaRepository<Permission,Integer> {



        public List<Permission> findPermissionsByPid(Integer pid);


        //
        public Permission findPermissionById(Integer id);

        //
        public Permission findPermissionByName(String name);

        public void deletePermissionById(Integer id);


}
