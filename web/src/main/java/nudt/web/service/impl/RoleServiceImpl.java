package nudt.web.service.impl;

import nudt.web.entity.Role;
import nudt.web.entity.RoleExtend;
import nudt.web.entity.ServiceRole;
import nudt.web.repository.RoleRepository;
import nudt.web.repository.ServiceRepository;
import nudt.web.repository.ServiceRoleRepository;
import nudt.web.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;




@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    ServiceRoleRepository serviceRoleRepository;

    @Override
    public Page<Role> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    @Override
    public long countAll() {
        return roleRepository.count();
    }

    @Override
    public Role findRoleByName(String name) {
        return roleRepository.findRoleByName(name);
    }

    @Override
    public Role findRoleById(Integer id) {
        return roleRepository.findOne(id);
    }

    @Override
    @Transactional
    public void deleteRoleById(Integer id) {
        roleRepository.deleteRoleById(id);
    }

    @Override
    public List<Role> findRoleByNameContains(String name) {
        return roleRepository.findRoleByNameContains(name);
    }

    @Override
    @Transactional
    public void deleteByIdIn(List<Integer> ids) {
        roleRepository.deleteByIdIn(ids);
    }

    @Override
    @Transactional
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> findRolesByIdIn(List<Integer> rids) {
        return roleRepository.findRolesByIdIn(rids);
    }

    //找到每一个roleid对应的业务服务器serviceName
    @Override
    public List<RoleExtend> findRoleExtends(List<Role> roles) {

        List<RoleExtend> roleExtends = new ArrayList<>();

        for(Role role:roles)
        {
            Integer rid = role.getId();
            ServiceRole serviceRole =serviceRoleRepository.findServiceRoleByRid(rid);
            Integer sid = serviceRole.getSid();

            String serviceName = serviceRepository.findServiceBySid(sid).getServiceName();

            RoleExtend re = new RoleExtend();
            re.setRoleid(rid);
            re.setRolename(role.getName());
            re.setRoledescription(role.getDescription());
            re.setServiceName(serviceName);
            roleExtends.add(re);
        }

        return roleExtends;
    }
}
