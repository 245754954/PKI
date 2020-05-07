package nudt.web.service.impl;

import nudt.web.entity.RolePermission;
import nudt.web.repository.RolePermissionRepository;
import nudt.web.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class RolePermissionServiceImpl implements RolePermissionService {


    @Autowired
    RolePermissionRepository rolePermissionRepository;
    @Override
    public List<RolePermission> findAllByRid(Integer rid) {
        return  rolePermissionRepository.findAllByRid(rid);
    }

    @Override
    @Transactional
    public void addRolePermission(RolePermission rolePermission) {
        rolePermissionRepository.save(rolePermission);
    }

    @Override
    public RolePermission findRolePermissionByRidAndPid(Integer rid, Integer pid) {
        return rolePermissionRepository.findRolePermissionByRidAndPid(rid,pid);
    }

    //根据角色id集合，找到用户的权限ids
    @Override
    public List<Integer> findPidsByRids(List<Integer> rids) {
        List<RolePermission> rps = new ArrayList<>();
        for(Integer rid:rids)
        {
           List<RolePermission> rp = rolePermissionRepository.findAllByRid(rid);
           rps.addAll(rp);
        }
        List<Integer> pids = new ArrayList<>();
        for(RolePermission r:rps)
        {
            pids.add(r.getPid());
        }

        return pids;
    }

    @Override
    @Transactional
    public void deleteAllByRid(Integer roleid) {
        rolePermissionRepository.deleteAllByRid(roleid);
    }
}
