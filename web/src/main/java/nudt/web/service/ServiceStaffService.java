package nudt.web.service;

import nudt.web.entity.Service;
import nudt.web.entity.ServiceStaff;
import nudt.web.repository.ServiceStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.odm.annotations.Attribute;

import java.util.List;

public interface ServiceStaffService {


    public ServiceStaff save(ServiceStaff serviceStaff);
    public  void deleteServiceStaffsByStaffId(Integer staffid);

    //根据service的id找到属于该service的所有staff 的id
    public List<Integer> findAllByServiceId(Integer serviceid);
}
