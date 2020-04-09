package nudt.web.repository;


import nudt.web.entity.Staff;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;


public interface StaffRepository  extends JpaRepository<Staff,Integer>{

    public Staff findStaffByUsername(String username);

    //根据员工的审核通过码来判定员工是否通过审核
    public List<Staff> findStaffByCode(Integer code);

    //通过个体的id来进行查询
    public Staff findStaffById(Integer id);

    //通过id删除员工
    public void deleteStaffById(Integer id);


    //模糊查询
    public List<Staff> findStaffByUsernameContains(String username);

    //批量删除
    public void deleteByIdIn(List<Integer> ids);

}
