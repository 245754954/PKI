package nudt.web.repository;

import nudt.web.entity.Role;
import nudt.web.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Integer> {

  public Role findRoleByName(String name);


  @Override
  public Role findOne(Integer id);

  //通过id删除role
  public void deleteRoleById(Integer id);


  //模糊查询
  public List<Role> findRoleByNameContains(String name);

  //批量删除
  public void deleteByIdIn(List<Integer> ids);


  public List<Role> findRolesByIdIn(List<Integer> rids);

}
