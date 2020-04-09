package nudt.web.repository;

import nudt.web.entity.StaffRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffRoleRepository extends JpaRepository<StaffRole,Integer> {

    //根据用户的id找到用户的roleid集合
    public List<StaffRole> findAllBySid(Integer sid);

    public void deleteStaffRoleByRidAndSid(Integer rid,Integer sid);
}
