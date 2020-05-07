package nudt.web.service.impl;

import nudt.web.repository.ServiceRepository;
import nudt.web.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    ServiceRepository serviceRepository;

    @Transactional
    @Override
    public nudt.web.entity.Service save(nudt.web.entity.Service service) {

        return  serviceRepository.save(service);
    }

    @Override
    public nudt.web.entity.Service findServiceByServiceName(String serviceName) {
        return serviceRepository.findServiceByServiceName(serviceName);
    }

    @Override
    public Integer findServiceIdByServiceName(String serviceName) {
       return serviceRepository.findServiceByServiceName(serviceName).getSid();
    }

    @Override
    public List<nudt.web.entity.Service> findAll() {
        return serviceRepository.findAll();
    }
}
