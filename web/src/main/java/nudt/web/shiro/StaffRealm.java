package nudt.web.shiro;


import nudt.web.entity.Permission;
import nudt.web.entity.Staff;
import nudt.web.service.PermissionService;
import nudt.web.service.RolePermissionService;
import nudt.web.service.StaffRoleService;
import nudt.web.service.StaffService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Security;
import java.util.List;


//员工的realm，这个将来由ShiroSecurityManager关联
public class StaffRealm  extends AuthorizingRealm {

    private static final Logger log =  LoggerFactory.getLogger(StaffRealm.class);
    @Autowired
    StaffService staffService;

    @Autowired
    StaffRoleService staffRoleService;

    @Autowired
    RolePermissionService rolePermissionService;

    @Autowired
    PermissionService permissionService;

    //执行授权逻辑
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.debug("execute Authorization!");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //采用info对象对资源授权
        Subject subject = SecurityUtils.getSubject();
        Staff staff = (Staff)subject.getPrincipal();
        //查询该用户拥有的权限
        List<Integer> rids = staffRoleService.findRidsBySid(staff.getId());

        List<Integer> pids = rolePermissionService.findPidsByRids(rids);

        List<Permission> perms =permissionService.findPermissionsByPid(pids);
        StringBuilder sb = new StringBuilder();
        if(perms!=null)
        {
                String str = perms.get(0).getUrl();
                if(null!=str)
                {
                    sb.append(str+",");
                }



            for(int i=1;i<perms.size()-1;i++)
            {
                String st = perms.get(i).getUrl();
                if(st!=null)
                {

                    sb.append(perms.get(i).getUrl()+",");
                }

            }
            if(perms.get(perms.size()-1).getUrl()!=null)
            {
                sb.append(perms.get(perms.size()-1).getUrl());
            }

        }
        System.out.println("the perms:"+sb.toString());
        info.addStringPermission(sb.toString());

        return info;
    }

    //执行身份认证逻辑
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.debug("execute Authentication!");

        //执行认证逻辑，也就是判断用户名密码是否存在
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
        String name = token.getUsername();
        //1:判断账号
        Staff staff = staffService.findStaffByUsername(name);
        if(null==staff)
        {
            //用户名不存在
            return  null;//shiro底层会抛出UnknownAccountException
        }
        //2：判断密码
        //只需要返回一个对象，shiro会自动判断密码，第二个参数就是密码
        //第一个三个参数可以为空
        return new SimpleAuthenticationInfo(staff,staff.getPassword(),"");
}


}
