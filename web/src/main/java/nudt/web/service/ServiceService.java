package nudt.web.service;


import nudt.web.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public interface ServiceService {

   public void save(nudt.web.entity.Service service);


    public nudt.web.entity.Service findServiceByServiceName(String serviceName);


    public Integer findServiceIdByServiceName(String serviceName);

}
