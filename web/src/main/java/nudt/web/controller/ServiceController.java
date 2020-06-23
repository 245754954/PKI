package nudt.web.controller;


import nudt.web.entity.*;
import nudt.web.service.*;
import nudt.web.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/ser")
public class ServiceController
{
    @Autowired
    ServiceService serviceService;

    @Autowired
    SysMenuService sysMenuService;

    @Autowired
    RoleService roleService;

    @Autowired
    ServiceRoleService serviceRoleService;


    @Autowired
    PermissionService permissionService;

    @Autowired
    RolePermissionService rolePermissionService;

    @Autowired
    StaffService staffService;

    @Autowired
    ServiceStaffService serviceStaffService;


    @Autowired
    ServicePermissionService servicePermissionService;

    //接受业务系统的注册，此时还应该为注册的业务系统开辟一个权限父菜单
    @RequestMapping(value = "/register",method = {RequestMethod.POST,RequestMethod.GET})
    public String register(HttpServletRequest request, Service service){

        if(serviceService.findServiceByServiceName(service.getServiceName())!=null)
        {
            return "service/login";
        }
        String ip  = RequestUtil.getUserIP(request);
        service.setServiceIP(ip);
        //保存业务
        service = serviceService.save(service);

        Permission p = new Permission();
        p.setChecked(true);
        p.setIcon("glyphicon glyphicon-user");
        p.setOpen(true);
        p.setPid(0);
        p.setName(service.getServiceName());
        //保存权限父菜单
        p = permissionService.save(p);


        //保存业务系统和权限之间的关系
        ServicePermission servicePermission = new ServicePermission();
        servicePermission.setPermissionId(p.getId());
        servicePermission.setServiceId(service.getSid());
        servicePermissionService.save(servicePermission);

        return "service/login";
    }
    //接受登录请求
    @RequestMapping(value = "/login",method = {RequestMethod.POST,RequestMethod.GET})
    public String login(HttpSession session, Model model, HttpServletRequest request, Service service){
        Service service1 = serviceService.findServiceByServiceName(service.getServiceName());
        if(null!=service1)
        {
            session.setAttribute("serviceName",service1.getServiceName());
            session.setMaxInactiveInterval(1800);
            model.addAttribute("serviceName",service.getServiceName());
            //request.setAttribute("serviceName",service.getServiceName());
            return "service/index-new";
        }else
        {
            model.addAttribute("serviceName","请登录！");
            return "service/login";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/addRole",method = {RequestMethod.POST,RequestMethod.GET})
    public Map addRole(@RequestParam("role_id")String role_id,@RequestParam("role_name")String role_name,@RequestParam("role_description")String role_description,HttpSession session,HttpServletRequest request, Service service){

        //如果某个角色存在那么就不能再添加了
        HashMap<String,Object> map = new HashMap();
        Role ro = roleService.findRoleByName(role_name);
        if(null!=ro)
        {
            map.put("resultCode","111");
            return map;
        }
        String serviceName = (String)session.getAttribute("serviceName");
        //保存角色信息，同时保存角色喝服务之间的关系
        Integer sid = serviceService.findServiceIdByServiceName(serviceName);
        Role r = new Role();
        r.setName(role_name);
        r.setDescription(role_description);
        Role r1 =roleService.save(r);
        //保存角色和service之间的对应关系
        ServiceRole serviceRole = new ServiceRole();
        serviceRole.setRid(r1.getId());
        serviceRole.setSid(sid);
        serviceRoleService.save(serviceRole);
        map.put("resultCode","000");
        return map;
    }


    @ResponseBody
    @RequestMapping(value = "/updateRole",method = {RequestMethod.POST,RequestMethod.GET})
    public Map updateRole(@RequestParam("role_id")String role_id,@RequestParam("role_name")String role_name,@RequestParam("role_description")String role_description,HttpSession session,HttpServletRequest request, Service service){

        //如果某个角色存在那么就不能再添加了
        HashMap<String,Object> map = new HashMap();
        Role ro = roleService.findRoleByName(role_name);

        ro.setDescription(role_description);
        ro.setName(role_name);
        roleService.save(ro);

        map.put("resultCode","000");
        return map;
    }






    //服务系统对自己本业务系统角色的处理
    @ResponseBody
    @RequestMapping(value = "/searchRole",method = {RequestMethod.POST,RequestMethod.GET})
    public Map searchRole(HttpSession session,@RequestParam("rows") int pageSize,
                             @RequestParam("page") int pageNo,HttpServletRequest request,HttpServletResponse response){
        //读取隶属于本业务系统的所有角色信息
        //首先查找到service的id
        //然后去servicerole表中找到隶属于该service的所有role id
        int total;//页面数
        Map rows = new HashMap();
        //读取隶属于本业务系统的所有角色名称
        String serviceName =(String)session.getAttribute("serviceName");

        Integer serviceID = serviceService.findServiceIdByServiceName(serviceName);
        //查找到隶属于该业务系统的所有角色的id
        List<Integer> rids = serviceRoleService.findRidsByServiceID(serviceID);
        //得到角色对象的列表
        List<Role> roles = roleService.findRolesByIdIn(rids);
        //记录总条数
        int recordNum = roles.size();
        //当前页面起止页码数
        int beginNo, endNo;
        beginNo = pageSize*(pageNo-1);
        endNo = beginNo+pageSize-1;
        if(endNo>=recordNum){
            endNo = recordNum-1;
        }
        //endNo = beginNo+pageSize-1>=recordNum?recordNum-1:beginNo+pageSize-1;
        //当前页面请求的tradelist
        List<Role> tempTradeList = new ArrayList<Role>();
        for(int i=beginNo; i<=endNo; i++){
            tempTradeList.add(roles.get(i));
        }
        total = (int) Math.ceil((double)recordNum/pageSize);
        rows.put("total", String.valueOf(total));
        rows.put("records", String.valueOf(recordNum));
        rows.put("rows", tempTradeList);
        rows.put("page", pageNo);
        request.setAttribute("serviceName",serviceName);
        return rows;
    }

    //得到系统所有的菜单，然后以json格式返回去
    @ResponseBody
    @RequestMapping(value = "/getMenu",method = {RequestMethod.POST,RequestMethod.GET})
    public List Menu(HttpSession session,HttpServletRequest request, Service service){
        String serviceName = (String)session.getAttribute("serviceName");

        Service service1 = serviceService.findServiceByServiceName(serviceName);
        List<Integer> serviceids = new ArrayList<>();
        serviceids.add(service1.getSid());
        List<Integer> pids = servicePermissionService.findPermissionIdsByServiceIDs(serviceids);


        List<Permission> permissions = permissionService.findPermissionsByPid(pids);
        return permissions;
    }

    //给某个角色分配权限的页面，并且需要标记出已经分配给该角色的权限，同时列出未分配的权限
    @RequestMapping(value = "/toRolePermissionPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toRolePermissionPage(Model model,HttpSession session,HttpServletRequest request, Service service,@RequestParam("role_id")Integer role_id){




            model.addAttribute("role_id",role_id);
            return "service/system/rolePermission";
    }


    //得到所有的权限信息，并且分为已经分配和未分配的
    //分配过的将checked设置为true，未分配的设置为false
    @ResponseBody
    @RequestMapping(value = "/getPermissionWithRole",method = {RequestMethod.POST,RequestMethod.GET})
    public Map<String, Object> getPermissionWithRole(
            HttpSession session,HttpServletRequest request, Service service,@RequestParam("role_id")Integer role_id){
        //找到本角色隶属于哪一个业务系统，找到业务系统的id
        List<Integer> serviceIds = serviceRoleService.findServiceIdsByRoleId(role_id);
        //找到这些业务系统下的所有权限信息
        List<Integer> pids = servicePermissionService.findPermissionIdsByServiceIDs(serviceIds);

        //找到次业务系统下面的权限信息
        HashMap<String,Object> map = new HashMap<>();
        //找到每一个业务系统的权限信息
        List<Permission> permissions = permissionService.findPermissionsByPid(pids);

        //找到用户已经分配的权限信息
        List<RolePermission> rolePermissions = rolePermissionService.findAllByRid(role_id);
        //该角色已经拥有的权限信息
        List<Integer> ownedPids = new ArrayList<>();
        for(RolePermission rp:rolePermissions)
        {
            ownedPids.add(rp.getPid());
        }

        for(Permission p:permissions)
        {
            if(ownedPids.contains(p.getId()))
            {
                p.setChecked(true);
            }
            else
            {
                p.setChecked(false);
            }
        }


        map.put("menus",permissions);
        return map;
    }



    //根据业务系统的名称来查询该业务系统下的用户名
    @ResponseBody
    @RequestMapping("/getStaffByServiceName")
    public Object getStaffByServiceName(Model model,String queryText, Integer pageno, Integer pagesize )
    {
        AJAXResult result = new AJAXResult();
        if(""==queryText||null==queryText)
        {
            try {

                // 分页查询
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("start", (pageno-1)*pagesize);
                map.put("size", pagesize);
                map.put("queryText", queryText);

                org.springframework.data.domain.Page<Staff> page = staffService.findAll(new PageRequest(pageno,pagesize));

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

                //首先
                Service service = serviceService.findServiceByServiceName(queryText);
                Integer serviceID = service.getSid();

                List<Integer> staffids = serviceStaffService.findAllByServiceId(serviceID);




                List<Staff> staffs = staffService.findAllByIdIn(staffids);



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





































































    //业务系统也能给本业务系统的用户分配角色，本方法到达业务系统给角色分配权限的页面
    @RequestMapping(value = "/toPermissionPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toPermissionPage(HttpServletRequest request, Service service){

        return "service/layout/west2";
    }


















































    //去业务系统添加权限菜单页面
    @RequestMapping(value = "/toAddMenuPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toAddMenuPage(HttpSession session,HttpServletRequest request, Service service){


        return "service/system/menus";
    }


    @RequestMapping(value = "/toRoleManagePage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toRoleManagePage(HttpSession session,HttpServletRequest request, Service service){
       String serviceName = (String)session.getAttribute("serviceName");
       if(null==serviceName)
       {
           return "redirect:/ser/toLoginPage";
       }




        request.setAttribute("serviceName",serviceName);



        return "service/system/roleManage";
    }
    @RequestMapping(value = "/toNorthPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toNorthPage(HttpServletRequest request, Service service){
       return "service/layout/north";
    }
    @RequestMapping(value = "/toWest2Page",method = {RequestMethod.POST,RequestMethod.GET})
    public String toWest2Page(HttpServletRequest request, Service service){
        return "service/layout/west2";
    }
    @RequestMapping(value = "/toCenterPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toCenterPage(HttpServletRequest request, Service service){
        return "service/layout/center";
    }
    @RequestMapping(value = "/toWestMenuPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toWestMenuPage(HttpServletRequest request, Service service){
        return "service/layout/west_menu";
    }
    @RequestMapping(value = "/toHomePage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toHomePage(HttpServletRequest request, Service service){
        return "service/layout/home";
    }
    @RequestMapping(value = "/toFormBasicPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toFormBasicPage(HttpServletRequest request, Service service){
        return "service/demo/form_basic";
    }
    @RequestMapping(value = "/toFormValidatePage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toFormValidatePage(HttpServletRequest request, Service service){
        return "service/demo/form_validate";
    }
    @RequestMapping(value = "/toTableJqgridPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toTableJqgridPage(HttpServletRequest request, Service service){
        return "service/demo/table_jqgrid";
    }
    @RequestMapping(value = "/toLayerdatePage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toLayerdatePage(HttpServletRequest request, Service service){
        return "service/demo/layerdate";
    }
    @RequestMapping(value = "/toSweetalertPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toSweetalertPage(HttpServletRequest request, Service service){
        return "service/demo/sweetalert";
    }
    @RequestMapping(value = "/toToastrNotificationsPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toToastrNotificationsPage(HttpServletRequest request, Service service){
        return "service/demo/toastr_notifications";
    }
    @RequestMapping(value = "/toButtonsPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toButtonsPage(HttpServletRequest request, Service service){
        return "service/demo/buttons";
    }
    @RequestMapping(value = "/toSpinnersPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toSpinnersPage(HttpServletRequest request, Service service){
        return "service/demo/spinners";
    }
    @RequestMapping(value = "/toIconsPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toIconsPage(HttpServletRequest request, Service service){
        return "service/demo/icons";
    }
    @RequestMapping(value = "/toFormFileUploadPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toFormFileUploadPage(HttpServletRequest request, Service service){
        return "service/demo/form_file_upload";
    }
    @RequestMapping(value = "/toGraphEchartsPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toGraphEchartsPage(HttpServletRequest request, Service service){
        return "service/demo/graph_echarts";
    }
    @RequestMapping(value = "/toExamplePagePage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toExamplePagePage(HttpServletRequest request, Service service){
        return "service/demo/example_page";
    }

    @RequestMapping(value = "/toListDictionaryPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toListDictionaryPage(HttpServletRequest request, Service service){
        return "service/demo/listDictionary";
    }
    @RequestMapping(value = "/toLoginPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toLoginPage()
    {
        return "service/login";
    }
    //到达业务系统注册页面
    @RequestMapping(value = "/toRegisterPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toRegisterPage()
    {
        return "service/register";
    }












}
