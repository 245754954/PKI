package nudt.web.repository;

import nudt.web.entity.Service;
import nudt.web.entity.SysMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service,Integer> {

    public Service findServiceByServiceName(String serviceName);

    public Service findServiceBySid(Integer sid);

}
