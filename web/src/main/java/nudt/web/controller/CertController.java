package nudt.web.controller;


import nudt.core.exception.CertException;
import nudt.web.config.AppConfig;
import nudt.web.config.CaConfig;
import nudt.web.dto.CertReq;
import nudt.web.entity.Permission;
import nudt.web.entity.Role;
import nudt.web.entity.Staff;
import nudt.web.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/ssl")
public class CertController {

    @Autowired
    CertService certService;

    @Autowired
    AppConfig appConfig;

    @Autowired
    StaffRoleService staffRoleService;

    @Autowired
    StaffService staffService;

    @Autowired
    RolePermissionService rolePermissionService;

    @Autowired
    PermissionService permissionService;


    @Autowired
    RoleService roleService;
    @ResponseBody
    @RequestMapping(value = "/auth",method = {RequestMethod.POST,RequestMethod.GET})
    public Map getClientCert(HttpServletRequest request) throws CertificateNotYetValidException, CertException {
        //获取客户端的数字证书
               X509Certificate[] certificate = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");
        System.out.println(certificate[0]);

        boolean b = certService.verifyCertificateValidity(certificate[0]);
        System.out.println("validaty:  "+b);
        CertReq req = new CertReq();
        req.setCatype(3);
        CaConfig caConfig = appConfig.getByKeyType(req.getCatype());

        boolean b1 = certService.verifyUserCert(certificate[0], caConfig.getPub());
        System.out.println("validity:  "+b1);

        HashMap<String,Object> map = new HashMap<>();
        map.put("msg","get client certificate success");
        return map ;
    }

    //根据用户的名称得到用户的角色信息
    @ResponseBody
    @RequestMapping(value = "/roles",method = {RequestMethod.POST,RequestMethod.GET})
    public List<Role> getUserRole(@RequestParam("username")String username, HttpServletRequest request, HttpServletResponse response){

        Integer sid =staffService.findStaffByUsername(username).getId();

        List<Integer> rids= staffRoleService.findRidsBySid(sid);
        List<Role> roles  = new ArrayList<>();
        for(Integer rid:rids)
        {
            roles.add(roleService.findRoleById(rid));
        }
        return roles;
    }

    //得到用户所有的权限信息
    //根据用户的名称得到用户的角色信息
    @ResponseBody
    @RequestMapping(value = "/permissions",method = {RequestMethod.POST,RequestMethod.GET})
    public Permission getUserPermission(@RequestParam("username")String username,HttpServletRequest request, HttpServletResponse response){


        //根据用户名查询用户角色，根据角色查询用户权限，能够查询到登录用户的全部权限信息
        Staff staff1 = staffService.findStaffByUsername(username);
        Integer sid = staff1.getId();
        //根据sid获取用户角色集合
        List<Integer> rids =staffRoleService.findRidsBySid(sid);
        //根据角色找到权限的集合信息
        List<Integer> pids = rolePermissionService.findPidsByRids(rids);
        //找到用户的所有的权限信息
        List<Permission> permissions = permissionService.findPermissionsByPid(pids);

        //将所有的权限保存到session
        Map<Integer, Permission> permissionMap = new HashMap<Integer, Permission>();

        Permission root = null;

        for ( Permission permission : permissions ) {
            //将用户所有的permission都放入map里边
            permissionMap.put(permission.getId(), permission);
        }

        for ( Permission permission : permissions ) {
            //把每一个permission都作为child
            Permission child = permission;
            if ( child.getPid() == 0 ) {
                //如果permission的pid为0，那么就代表根节点
                root = permission;
            } else {
                //否则就作为子节点
                Permission parent = permissionMap.get(child.getPid());
                //将子节点加入父节点
                parent.getChildren().add(child);
            }
        }
    return root;

    }









}
