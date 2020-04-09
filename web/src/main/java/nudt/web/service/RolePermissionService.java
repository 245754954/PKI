package nudt.web.service;

import nudt.web.entity.RolePermission;

import java.util.List;

public interface RolePermissionService {

    public List<RolePermission> findAllByRid(Integer rid);

    public void addRolePermission(RolePermission rolePermission);


    public RolePermission findRolePermissionByRidAndPid(Integer rid,Integer pid);

    //根据角色id集合，找到用户的权限ids
    public List<Integer> findPidsByRids(List<Integer> rids);

}
