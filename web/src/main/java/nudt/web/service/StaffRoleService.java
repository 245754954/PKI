package nudt.web.service;

import nudt.web.entity.StaffRole;

import java.util.List;

public interface StaffRoleService {

    //根据用户的id找到用户的roleid集合
    public List<StaffRole> findAllBySid(Integer sid);

    //给用户增加角色
    public void assignRoleToStaff(Integer sid,Integer[]rids);
    //给用户消减角色

    public void removeRoleFromStaff(Integer sid,Integer[] rids);

    //根据用户的sid来查询用户的角色id集合
    public  List<Integer> findRidsBySid(Integer sid);

}
