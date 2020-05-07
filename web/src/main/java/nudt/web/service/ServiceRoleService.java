package nudt.web.service;

import nudt.web.entity.ServiceRole;

import java.util.List;

public interface ServiceRoleService {

        public void save(ServiceRole serviceRole);

        public List<Integer> findRidsByServiceID(Integer sid);

        public void deleteByRid(Integer rid);

        public  List<Integer> findServiceIdsByRoleId(Integer roleid);

}
