package nudt.web.service;


import nudt.web.entity.Role;
import nudt.web.entity.RoleExtend;
import nudt.web.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface RoleService {

    public Page<Role> findAll(Pageable pageable);

    //计算总的数据数量
    public long countAll();

    public Role findRoleByName(String name);



    //通过个体的id来进行查询
    public Role findRoleById(Integer id);

    //通过id删除role
    public void deleteRoleById(Integer id);


    //模糊查询
    public List<Role> findRoleByNameContains(String name);

    //批量删除
    public void deleteByIdIn(List<Integer> ids);

    public Role save(Role role);

    public List<Role> findAll();

    //查找到隶属于该业务系统的所有角色信息
    public List<Role> findRolesByIdIn(List<Integer> rids);


    //找到每一个roleid对应的业务服务器serviceName
    public List<RoleExtend> findRoleExtends(List<Role> roles);
}
