package nudt.web.controller;


import nudt.web.entity.*;
import nudt.web.service.RolePermissionService;
import nudt.web.service.RoleService;
import nudt.web.service.ServiceRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping(value = "/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @Autowired
    RolePermissionService rolePermissionService;

    @Autowired
    ServiceRoleService serviceRoleService;



    //到达用user主界面
    @RequestMapping(value = "/toRolePage",method = {RequestMethod.POST,RequestMethod.GET})
    public String index(Model model, @RequestParam(required = false,defaultValue = "1") Integer pageno, @RequestParam(required = false,defaultValue = "2") Integer pagesize){
        //进行分页查询，需要两个参数，pageNo ,pageSize
        HashMap<String,Object> map = new HashMap<>();
        Integer start=(pageno-1)*pagesize;
        Integer size = pagesize;
        Page page = roleService.findAll(new PageRequest(pageno,pagesize));
        List<Role> roles = page.getContent();

//        List<RoleExtend> roleExtends = roleService.findRoleExtends(roles);
//
//        System.out.println("roleExtends"+roleExtends);

//        System.out.println("roles"+roles);
        model.addAttribute("roles",roles);
        //当前页码
        model.addAttribute("pageno",pageno);
        //每一页的最大size
        model.addAttribute("pagesize",pagesize);

        //总的数据条数
        long totalsize =roleService.countAll() ;
        //最大页码
        long totalpage =0;
        if(totalsize%pagesize==0)
        {
            totalpage = totalsize/pagesize;

        }else
        {
            totalpage = totalsize/pagesize+1;
        }
        model.addAttribute("totalno",totalpage);
        //使用limit 需要两个参数，start和size
        return  "authorization/role/index";
    }


    @ResponseBody
    @RequestMapping("/pageQuery")
    public Object pageQuery( String queryText, Integer pageno, Integer pagesize ) {



        AJAXResult result = new AJAXResult();
        if(""==queryText||null==queryText)
        {
            try {

                // 分页查询
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("start", (pageno-1)*pagesize);
                map.put("size", pagesize);
                map.put("queryText", queryText);

                Page<Role> page = roleService.findAll(new PageRequest(pageno,pagesize));

                List<Role> roles = page.getContent();// 当前页码

                // 总的数据条数
                long totalsize = roleService.countAll();
                // 最大页码（总页码）
                long totalno = 0;
                if ( totalsize % pagesize == 0 ) {
                    totalno = totalsize / pagesize;
                } else {
                    totalno = totalsize / pagesize + 1;
                }
                List<RoleExtend> roleExtends = roleService.findRoleExtends(roles);
                // 分页对象
                nudt.web.entity.Page<RoleExtend> rolePage = new nudt.web.entity.Page<RoleExtend>();
                rolePage.setDatas(roleExtends);
                rolePage.setTotalno(Integer.parseInt(totalno+""));
                rolePage.setTotalsize(Integer.parseInt(totalsize+""));
                rolePage.setPageno(pageno);

                result.setData(rolePage);
                result.setSuccess(true);
            } catch ( Exception e ) {
                e.printStackTrace();
                result.setSuccess(false);
            }

            return result;


        }else{
            try {

                // 分页查询
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("start", (pageno-1)*pagesize);
                map.put("size", pagesize);
                map.put("queryText", queryText);

                List<Role> roles = roleService.findRoleByNameContains(queryText);



                // 总的数据条数
                long totalsize = roles.size();
                // 最大页码（总页码）
                long totalno = 0;
                if ( totalsize % pagesize == 0 ) {
                    totalno = totalsize / pagesize;
                } else {
                    totalno = totalsize / pagesize + 1;
                }
                List<RoleExtend> roleExtends = roleService.findRoleExtends(roles);
                // 分页对象
                nudt.web.entity.Page<RoleExtend> rolePage = new nudt.web.entity.Page<RoleExtend>();
                rolePage.setDatas(roleExtends);
                rolePage.setTotalno(Integer.parseInt(totalno+""));
                rolePage.setTotalsize(Integer.parseInt(totalsize+""));
                rolePage.setPageno(pageno);

                result.setData(rolePage);
                result.setSuccess(true);
            } catch ( Exception e ) {
                e.printStackTrace();
                result.setSuccess(false);
            }

            return result;

        }

    }

    //到达添加用户的界面
    @RequestMapping(value = "/toAddRolePage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toAddUserPage(){

        return "authorization/role/add";
    }


    @ResponseBody
    @RequestMapping("/insert")
    public Object insert(Role role ) {
        AJAXResult result = new AJAXResult();

        Role role1 = roleService.findRoleByName(role.getName());
        if(null!=role1)
        {
            result.setSuccess(false);

            return result;
        }

        try {
            roleService.save(role);

            result.setSuccess(true);
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/delete")
    public Object delete( Integer id ) {
        AJAXResult result = new AJAXResult();

        try {

            roleService.deleteRoleById(id);

            //删除ServiceRole表中的关系
            serviceRoleService.deleteByRid(id);
            result.setSuccess(true);
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/deletes")
    public Object deletes( Integer[] roleid ) {
        AJAXResult result = new AJAXResult();
        List<Integer> ids = Arrays.asList(roleid);
        try {
            roleService.deleteByIdIn(ids);
            result.setSuccess(true);
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;
    }

    @RequestMapping(value = "/toEditPage",method = {RequestMethod.GET,RequestMethod.POST})
    public String toEditPage(Model model, Integer id, HttpServletRequest request , HttpServletResponse response){

        Role role =roleService.findRoleById(id);

        model.addAttribute("role",role);
        return "authorization/role/edit";
    }

    @ResponseBody
    @RequestMapping(value = "/update",method = {RequestMethod.GET,RequestMethod.POST})
    public AJAXResult update(Role role){
        AJAXResult result = new AJAXResult();

        roleService.save(role);
        result.setSuccess(true);
        return result;
    }



    @RequestMapping("/assign")
    public String assign(Integer id,Model model) {

        Role r = roleService.findRoleById(id);
        model.addAttribute("role",r);

        return "authorization/role/assign";
    }

    //给角色分配权限
    @ResponseBody
    @RequestMapping("/doAssign")
    public Object doAssign( Integer roleid, Integer[] permissionids ) {
        AJAXResult result = new AJAXResult();

        try {

            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("roleid", roleid);
            paramMap.put("permissionids", permissionids);

            for(Integer pid:permissionids)
            {
                RolePermission rp = rolePermissionService.findRolePermissionByRidAndPid(roleid,pid);
                if(null==rp)
                {
                    RolePermission rolePermission = new RolePermission();
                    rolePermission.setRid(roleid);
                    rolePermission.setPid(pid);
                    rolePermissionService.addRolePermission(rolePermission);

                }
            }

            result.setSuccess(true);
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;
    }


}
