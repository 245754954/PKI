package nudt.web.service.impl;

import nudt.web.entity.StaffRole;
import nudt.web.repository.StaffRepository;
import nudt.web.repository.StaffRoleRepository;
import nudt.web.service.StaffRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class StaffRoleServiceImpl implements StaffRoleService {

    @Autowired
    StaffRoleRepository staffRoleRepository;
    @Override
    public List<StaffRole> findAllBySid(Integer sid) {
        return staffRoleRepository.findAllBySid(sid);
    }

    @Override
    @Transactional
    public void assignRoleToStaff(Integer sid, Integer[] rids) {

        List<Integer> ids = Arrays.asList(rids);

        for(Integer id:ids)
        {
            StaffRole staffRole = new StaffRole();
            staffRole.setSid(sid);
            staffRole.setRid(id);
            staffRoleRepository.save(staffRole);
        }

    }

    @Override
    @Transactional
    public void removeRoleFromStaff(Integer sid, Integer[] rids) {
        List<Integer> ids = Arrays.asList(rids);
        for(Integer id:ids)
        {
            staffRoleRepository.deleteStaffRoleByRidAndSid(id,sid);

        }

    }
    //根据用户的sid来查询用户的角色id集合
    @Override
    public List<Integer> findRidsBySid(Integer sid) {

        List<StaffRole> sr = staffRoleRepository.findAllBySid(sid);
        List<Integer> ids = new ArrayList<>();
        for(StaffRole st:sr)
        {
            ids.add(st.getRid());
        }

        return ids;
    }


}
