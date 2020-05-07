package nudt.web.repository;

import nudt.web.entity.ServiceStaff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceStaffRepository extends JpaRepository<ServiceStaff,Integer> {

    public  void deleteServiceStaffsByStaffId(Integer staffid);

    public List<ServiceStaff> findAllByServiceId(Integer serviceid);

}
