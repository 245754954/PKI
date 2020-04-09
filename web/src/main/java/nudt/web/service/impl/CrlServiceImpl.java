package nudt.web.service.impl;

import nudt.web.entity.CRL;
import nudt.web.repository.CRLRepository;
import nudt.web.service.CrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrlServiceImpl implements CrlService {

    @Autowired
    CRLRepository crlRepository;


    @Override
    public void Add(CRL crl) {
        crlRepository.save(crl);
    }

    @Override
    public List<CRL> findAll() {
        return crlRepository.findAll();
    }

}
