package nudt.web.service.impl;

import nudt.web.entity.SysMenu;
import nudt.web.repository.SysMenuRepository;
import nudt.web.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    SysMenuRepository sysMenuRepository;

    @Override
    public List<SysMenu> findAll() {
        return sysMenuRepository.findAll();
    }
}
