package nudt.web.service.impl;


import nudt.web.entity.ServicePermission;
import nudt.web.repository.ServicePermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicePermissionServiceImpl implements nudt.web.service.ServicePermissionService{

    @Autowired
    ServicePermissionRepository servicePermissionRepository;


    //保存权限和业务系统之间的关系
    @Override
    @Transactional
    public ServicePermission save(ServicePermission servicePermission) {
        return servicePermissionRepository.save(servicePermission);
    }

    @Override
    public List<Integer> findPermissionIdsByServiceIDs(List<Integer> serviceids) {

        List<ServicePermission> servicePermissions = servicePermissionRepository.findServicePermissionsByServiceIdIn(serviceids);
        List<Integer> pids = new ArrayList<>();
        for(ServicePermission servicePermission:servicePermissions)
        {
            pids.add(servicePermission.getPermissionId());
        }

        return pids;
    }

    @Override
    @Transactional
    public void deleteByPAndPermissionId(Integer pid) {
        servicePermissionRepository.deleteByPermissionId(pid);
    }
}
