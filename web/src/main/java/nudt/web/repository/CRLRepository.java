package nudt.web.repository;

import nudt.web.entity.CRL;
import nudt.web.service.CrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface  CRLRepository  extends JpaRepository<CRL,String>{


    

    @Override
    public List<CRL> findAll();
}
