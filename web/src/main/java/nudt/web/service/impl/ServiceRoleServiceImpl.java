package nudt.web.service.impl;

import nudt.web.entity.ServiceRole;
import nudt.web.repository.ServiceRoleRepository;
import nudt.web.service.RoleService;
import nudt.web.service.ServiceRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceRoleServiceImpl implements ServiceRoleService {

    @Autowired
    ServiceRoleRepository serviceRoleRepository;

    @Override
    @Transactional
    public void save(ServiceRole serviceRole) {
        serviceRoleRepository.save(serviceRole);
    }

    @Override
    public List<Integer> findRidsByServiceID(Integer sid) {

        List<ServiceRole> serviceRoles = serviceRoleRepository.findAllBySid(sid);
        List<Integer> rids = new ArrayList<>();

        for(ServiceRole s:serviceRoles)
        {
            rids.add(s.getRid());
        }
        return rids;
    }


    @Transactional
    @Override
    public void deleteByRid(Integer rid) {
        serviceRoleRepository.deleteByRid(rid);
    }

    @Override
    public List<Integer> findServiceIdsByRoleId(Integer roleid) {
        List<ServiceRole> serviceRoles = serviceRoleRepository.findServiceRolesByRid(roleid);

        List<Integer> serviceids = new ArrayList<>();

        for(ServiceRole serviceRole:serviceRoles)
        {
            serviceids.add(serviceRole.getSid());
        }

        return  serviceids;
    }
}
