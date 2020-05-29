package nudt.web.service.impl;

import nudt.web.entity.ServiceStaff;
import nudt.web.repository.ServiceStaffRepository;
import nudt.web.service.ServiceStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class ServiceStaffServiceImpl implements ServiceStaffService {

    @Autowired
    ServiceStaffRepository serviceStaffRepository;

    @Override
    @Transactional
    public ServiceStaff save(ServiceStaff serviceStaff) {
        return serviceStaffRepository.save(serviceStaff);
    }

    @Override
    @Transactional
    public void deleteServiceStaffsByStaffId(Integer staffid) {
        serviceStaffRepository.deleteServiceStaffsByStaffId(staffid);
    }

    //根据service的id找到属于该service的所有staff 的id
    @Override
    public List<Integer> findAllByServiceId(Integer serviceid) {

        List<ServiceStaff> serviceStaffs = serviceStaffRepository.findAllByServiceId(serviceid);
        List<Integer> staffids = new ArrayList<>();
        for(ServiceStaff serviceStaff:serviceStaffs)
        {
            staffids.add(serviceStaff.getStaffId());
        }
        return staffids;
    }

    @Override
    public List<ServiceStaff> findAllByStaffId(Integer staffid) {
        return serviceStaffRepository.findAllByStaffId(staffid);
    }

    //根据staffid删除service和staff之间的所属关系

}
