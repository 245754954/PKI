package nudt.web.service;


import nudt.web.entity.Permission;

import java.util.List;

public interface PermissionService {

    public List<Permission> findPermissionsByPid(Integer pid);

    public List<Permission> findAll();

    public Permission findPermissionById(Integer id);

    public  void addPermission(Permission permission);

    public Permission findPermissionByName(String name);

    public void deletePermissionById(Integer id);

    public List<Permission> findPermissionsByPid(List<Integer> pids);
}
