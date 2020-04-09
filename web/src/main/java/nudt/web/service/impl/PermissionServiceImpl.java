package nudt.web.service.impl;


import nudt.web.entity.Permission;
import nudt.web.repository.PermissionRepository;
import nudt.web.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    PermissionRepository permissionRepository;


    //父节点为空的话就代表根节点
    @Override
    public List<Permission> findPermissionsByPid(Integer pid) {
        return permissionRepository.findPermissionsByPid(pid);
    }

    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public Permission findPermissionById(Integer id) {
       return permissionRepository.findPermissionById(id);
    }

    @Override
    @Transactional
    public void addPermission(Permission permission) {
        permissionRepository.save(permission);
    }

    @Override
    public Permission findPermissionByName(String name) {
        return permissionRepository.findPermissionByName(name);
    }

    @Override
    @Transactional
    public void deletePermissionById(Integer id) {
        permissionRepository.deletePermissionById(id);
    }

    @Override
    public List<Permission> findPermissionsByPid(List<Integer> pids) {
       List<Permission> permissions = new ArrayList<>();
        for(Integer pid:pids)
        {
           Permission permission = permissionRepository.findPermissionById(pid);
            permissions.add(permission);
        }


        return permissions;
    }
}
