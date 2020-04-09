package nudt.web.controller;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonView;
import nudt.web.anno.UserNotExistException;
import nudt.web.config.AppConfig;
import nudt.web.dao.DepartmentDao;
import nudt.web.dao.EmployeeDao;
import nudt.web.entity.Department;
import nudt.web.entity.Employee;
import nudt.web.entity.Staff;
import nudt.web.entity.StaffBen;
import nudt.web.repository.StaffRepository;
import nudt.web.service.StaffService;
import nudt.web.util.ContactAttributeMapperJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Controller
@RequestMapping("/clerk")
public class ClerkController {

    @Autowired
    EmployeeDao employeeDao;

    @Autowired
    DepartmentDao departmentDao;

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    StaffService staffService;

    @Autowired
    LdapTemplate ldapTemplate;

    @Autowired
    AppConfig appConfig;

    //我们用于测试自定义异常
    @ResponseBody
    @RequestMapping(value = "/hello",method = {RequestMethod.GET,RequestMethod.POST})
    public String hello(@RequestParam("user")String user)throws UserNotExistException{

        throw new UserNotExistException("500","用户不正确");

    }

    //首页跳转
    @RequestMapping(value = {"/index","index.html","/"})
   public String index(){

       return "clerk/index";
   }


   //index页面的登录请求处理
//    @DeleteMapping(value = "")
//    @PutMapping(value = "")
//    @GetMapping(value = "")


    //管理员登录
  @RequestMapping(value = "/dashboard",method = {RequestMethod.POST,RequestMethod.GET})
   //@PostMapping(value = "/dashboard")//代表我们要映射一个post请求
   public String dashboard(HttpServletRequest request,HttpServletResponse response,HttpSession session,Map<String , Object>maps, @RequestParam("username")String username, @RequestParam("password")String password)
  {

      if (username.equals(appConfig.getUsername()) & password.equals(appConfig.getPassword()))
      {

          session.setAttribute("loginUser", username);

          JSONObject result = new JSONObject();
          //设置用户的cookie
          Cookie cookie=new Cookie("username",username);
          Cookie cookie1=new Cookie("password",password);
          cookie.setMaxAge(1800);
          cookie1.setMaxAge(1800);
          response.addCookie(cookie);
          response.addCookie(cookie1);

          //为了防止表单重复提交，我们此处采用了重定向
          return "redirect:/clerk/dashboard1";
      }
        else
        {
            maps.put("msg","管理员账号或者密码错误,请联系RA部门");
            return "clerk/index";
        }
  }


   @RequestMapping(value = "/dashboard1")
   public String dashboard1(){
        return "clerk/dashboard";
   }

   //查询所有员工，返回列表
    @GetMapping(value = "/emps")
   public  String list(Model model){

        //查询还没有审核通过的员工的资料,返回为列表的形式
       List<Staff> staffs =  staffService.findStaffByCode(0);

        model.addAttribute("staffs",staffs);
        return "clerk/list";
   }

   @RequestMapping(value = "/toAddUnitPage",method = {RequestMethod.GET,RequestMethod.POST})
   public String toAddUnitPage(HttpServletRequest request,HttpServletResponse response){

        return "clerk/addPage";
   }

    @RequestMapping(value = "/addUnit",method = {RequestMethod.POST,RequestMethod.GET})
   public String addUnit(Map map,Staff staff,HttpServletRequest request,HttpServletResponse response){
        //不能存在已经添加的个体组织，必须要username唯一
        String username = staff.getUsername();
       Staff st =  staffService.findStaffByUsername(username);
       if(null!=st){
            map.put("msg","个体组织已经通过网站注册申请，无需再次注册!");
            return "clerk/list";

       }
        staffService.save(staff);
        return "redirect:/clerk/emps";

    }


  //用户点击添加人员，返回到添加页面
   @GetMapping(value = "/emp")
   public String addPage(Model model){
        //来到添加页面之前我们需要查出所有的部门
       Collection<Department> departments = departmentDao.getDepartments();
       model.addAttribute("depts",departments);
       return "clerk/add";
   }

   //这个对象用来接收前端的参数，唯一的要求就是前端的名字要和对象里边的名字一样，大小写都很重要
    @PostMapping(value = "/emp")
   public  String addEmp(Employee employee){

       // System.out.println("保存的员工"+employee);
        employeeDao.save(employee);
        //员工添加完成以后需要刷新
        //redirect:/clerk/emps表示重定向到一个地址
        //forward:/clerk/emps表示转发到一个地址
        return  "redirect:/clerk/emps";
   }


   //到修改页面，需要先将某id相关 的员工的信息查询出来，然后再到修改页面
    @GetMapping(value = "/emp/{id}")
    public String toEditPage(@PathVariable("id")Integer id,Model model){
             //查询出员工的相关信息
        Staff staff = staffService.findStaffById(id);
        model.addAttribute("staff",staff);
        //回到修改页面
        return "clerk/edit";
    }

    @PutMapping(value = "/emp")
    public  String updateEmployee(Staff staff){

        //employeeDao.save(employee);
        //保存审核通过的用户信息
         System.out.println(staff);
         staffService.save(staff);
        //返回员工列表页面
        return "redirect:/clerk/emps";
    }

    //员工删除
    @DeleteMapping(value = "/emp/{id}")
    public  String delete(@PathVariable("id")Integer id){

//        employeeDao.delete(id);
        staffService.deleteStaffById(id);
        return "redirect:/clerk/emps";
    }

    //注册的请求
    @RequestMapping(value = "/register",method = {RequestMethod.POST,RequestMethod.GET})
    public String register(HashMap map,StaffBen staffBen){
        //将接收到的数据注册到LDAP数据库

//        System.out.println(clerk);
//        //为待添加的数据构建dn
//        Name dn = LdapNameBuilder.newInstance()
//                .add("o", "CA")
//                .add("ou",clerk.getOrganization())
//                .add("cn",clerk.getUsername())
//                .build();
//        //构建属性
//        Attributes attrs = new BasicAttributes();
//        BasicAttribute ocattr = new BasicAttribute("objectclass");
//        ocattr.add("top");
//        ocattr.add("person");
//        attrs.put(ocattr);
//        attrs.put("cn", clerk.getUsername());
//        attrs.put("sn", clerk.getUsername());
//        attrs.put("userPassword",clerk.getPassword());
 //       ldapTemplate.bind(dn,null,attrs);
        Staff s = staffService.findStaffByUsername(staffBen.getUsername());
        if(null!=s){
            map.put("msg","用户已经存在");
            return  "cert/login";
        }

        Staff staff = new Staff();
        staff.setCountrycode(staffBen.getCountrycode());
        staff.setProvince(staffBen.getProvince());
        staff.setCity(staffBen.getCity());
        staff.setEmail(staffBen.getEmail());
        staff.setOrganization(staffBen.getOrganization());
        staff.setDepartment(staffBen.getDepartment());
        staff.setUsername(staffBen.getUsername());
        staff.setPassword(staffBen.getPassword());
        staff.setTelephone(staffBen.getTelephone());
        staff.setAddress(staffBen.getAddress());
        staff.setCode(0);
        staffService.save(staff);
        return  "cert/login";
    }


    //跳转到注册页面
    @RequestMapping(value = "/toRegisterPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toRegisterPage1(Map maps){

//        LdapQuery ldapQuery = query()
//                .base("o=CA")
//                .attributes("ou")
//                .where("objectClass").is("organizationalUnit");
//        List<String> lists = ldapTemplate.search(ldapQuery, new AttributesMapper<String>() {
//            @Override
//            public String mapFromAttributes(Attributes attributes) throws NamingException {
//
//                return (String) attributes.get("ou").get();
//
//            }
//        });

        List<String> lists = new ArrayList<>();
        lists.add("NUDT");
        lists.add("NUD");
        lists.add("MAS");
        maps.put("ou",lists);
        return  "clerk/register";
    }

    @RequestMapping(value = "/pass",method = {RequestMethod.POST,RequestMethod.GET})
    public String findPass(Model model,HttpServletResponse response, HttpServletRequest request){
        //查询还没有审核通过的员工的资料,返回为列表的形式
        List<Staff> staffs =  staffService.findStaffByCode(1);

        model.addAttribute("staffs",staffs);
        return "clerk/pass";

    }


    //到达权限主页
    @RequestMapping(value = "/toMainPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toMainPage(HttpSession session,Map map,HttpServletResponse response,HttpServletRequest request){

        String username = (String)session.getAttribute("loginUser");
        map.put("username",username);
        return "authorization/main";
    }




}
