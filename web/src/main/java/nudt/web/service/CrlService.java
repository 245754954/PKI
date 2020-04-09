package nudt.web.service;


import nudt.web.entity.CRL;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CrlService {

    
    public void Add(CRL crl);


    //查询已经撤销的证书列表
    public List<CRL> findAll();
}
