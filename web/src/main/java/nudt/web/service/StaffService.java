package nudt.web.service;


import nudt.web.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StaffService {

    //保存注册人员
    public Staff save(Staff staff);
    //根据用户名字注册
    public Staff findStaffByUsername(String username);

    //根据员工的审核通过码来判定员工是否通过审核
    public List<Staff> findStaffByCode(Integer code);

    //通过个体的id来进行查询
    public Staff findStaffById(Integer id);

    //通过id删除员工
    public void deleteStaffById(Integer id);

    //分页查询
    public Page<Staff> findAll(Pageable pageable);

    //计算总的数据数量
    public long countAll();

    //模糊查询
    public List<Staff> findStaffByUsernameContains(String username);

    //批量删除
    public void deleteByIdIn(List<Integer> ids);
}
