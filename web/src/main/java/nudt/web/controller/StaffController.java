package nudt.web.controller;

import java.util.*;
import java.util.stream.Collectors;
import nudt.web.entity.AJAXResult;
import nudt.web.entity.Role;
import nudt.web.entity.Staff;
import nudt.web.entity.StaffRole;
import nudt.web.service.RoleService;
import nudt.web.service.StaffRoleService;
import nudt.web.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/st")
public class StaffController {

    @Autowired
    StaffService staffService;

    @Autowired
    RoleService roleService;

    @Autowired
    StaffRoleService staffRoleService;

    @RequestMapping(value = "/find",method = {RequestMethod.GET,RequestMethod.POST})
    public void findAll(){

        return ;
    }

    //到达用user主界面
    @RequestMapping(value = "/user",method = {RequestMethod.POST,RequestMethod.GET})
    public String index(Model model, @RequestParam(required = false,defaultValue = "1") Integer pageno, @RequestParam(required = false,defaultValue = "2") Integer pagesize){
        //进行分页查询，需要两个参数，pageNo ,pageSize
        HashMap<String,Object> map = new HashMap<>();
        Integer start=(pageno-1)*pagesize;
        Integer size = pagesize;
        Page page = staffService.findAll(new PageRequest(pageno,pagesize));
        List<Staff> staffs = page.getContent();

        model.addAttribute("staffs",staffs);
        //当前页码
        model.addAttribute("pageno",pageno);
        //每一页的最大size
        model.addAttribute("pagesize",pagesize);

        //总的数据条数
        long totalsize =staffService.countAll() ;
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
        return "authorization/user/user";
    }


    //到达添加用户的界面
    @RequestMapping(value = "/toAddUserPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toAddUserPage(){

        return "authorization/user/add";
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

                Page<Staff> page = staffService.findAll(new PageRequest(pageno,pagesize));

                List<Staff> staffs = page.getContent();// 当前页码

                // 总的数据条数
                long totalsize = staffService.countAll();
                // 最大页码（总页码）
                long totalno = 0;
                if ( totalsize % pagesize == 0 ) {
                    totalno = totalsize / pagesize;
                } else {
                    totalno = totalsize / pagesize + 1;
                }

                // 分页对象
                nudt.web.entity.Page<Staff> staffPage = new nudt.web.entity.Page<Staff>();
                staffPage.setDatas(staffs);
                staffPage.setTotalno(Integer.parseInt(totalno+""));
                staffPage.setTotalsize(Integer.parseInt(totalsize+""));
                staffPage.setPageno(pageno);

                result.setData(staffPage);
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

                List<Staff> staffs = staffService.findStaffByUsernameContains(queryText);



                // 总的数据条数
                long totalsize = staffs.size();
                // 最大页码（总页码）
                long totalno = 0;
                if ( totalsize % pagesize == 0 ) {
                    totalno = totalsize / pagesize;
                } else {
                    totalno = totalsize / pagesize + 1;
                }

                // 分页对象
                nudt.web.entity.Page<Staff> staffPage = new nudt.web.entity.Page<Staff>();
                staffPage.setDatas(staffs);
                staffPage.setTotalno(Integer.parseInt(totalno+""));
                staffPage.setTotalsize(Integer.parseInt(totalsize+""));
                staffPage.setPageno(pageno);

                result.setData(staffPage);
                result.setSuccess(true);
            } catch ( Exception e ) {
                e.printStackTrace();
                result.setSuccess(false);
            }

            return result;

        }

    }

    @ResponseBody
    @RequestMapping("/insert")
    public Object insert(Staff staff ) {
        AJAXResult result = new AJAXResult();

        try {
            staffService.save(staff);

            result.setSuccess(true);
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;
    }

    @RequestMapping(value = "/toEditPage",method = {RequestMethod.GET,RequestMethod.POST})
    public String toEditPage(Model model,Integer id,HttpServletRequest re , HttpServletResponse response){
        Staff staff = staffService.findStaffById(id);
        model.addAttribute("staff",staff);
        return "authorization/user/edit";
    }

    @ResponseBody
    @RequestMapping(value = "/update",method = {RequestMethod.GET,RequestMethod.POST})
    public AJAXResult update(Staff staff){
        AJAXResult result = new AJAXResult();

        staffService.save(staff);
        result.setSuccess(true);
        return result;
    }


    @ResponseBody
    @RequestMapping("/delete")
    public Object delete( Integer id ) {
        AJAXResult result = new AJAXResult();

        try {

            staffService.deleteStaffById(id);
            result.setSuccess(true);
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/deletes")
    public Object deletes( Integer[] userid ) {
        AJAXResult result = new AJAXResult();
        List<Integer> ids = Arrays.asList(userid);
        try {
            staffService.deleteByIdIn(ids);
            result.setSuccess(true);
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;
    }


    @RequestMapping("/assign")
    public String assign( Integer id, Model model ) {

        Staff staff = staffService.findStaffById(id);

        model.addAttribute("staff", staff);

        List<Role> roles =roleService.findAll();

        List<Role> assingedRoles = new ArrayList<Role>();
        List<Role> unassignRoles = new ArrayList<Role>();

        List<StaffRole> staffRoles = staffRoleService.findAllBySid(id);

        // 获取关系表的数据
        List<Integer> roleids =new ArrayList<>();

        for(StaffRole st:staffRoles)
        {
            roleids.add(st.getRid());
        }

        for ( Role role : roles ) {
            if ( roleids.contains(role.getId()) ) {
                assingedRoles.add(role);
            } else {
                unassignRoles.add(role);
            }
        }

        model.addAttribute("assingedRoles", assingedRoles);
        model.addAttribute("unassignRoles", unassignRoles);

        return "authorization/user/assign";
    }


    @ResponseBody
    @RequestMapping("/doAssign")
    public Object doAssign( Integer userid, Integer[] unassignroleids ) {
        AJAXResult result = new AJAXResult();

        try {
            // 增加关系表数据
            staffRoleService.assignRoleToStaff(userid,unassignroleids);
            result.setSuccess(true);
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/dounAssign")
    public Object dounAssign( Integer userid, Integer[] assignroleids ) {
        AJAXResult result = new AJAXResult();

        try {

           staffRoleService.removeRoleFromStaff(userid,assignroleids);
            result.setSuccess(true);
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;
    }



}
