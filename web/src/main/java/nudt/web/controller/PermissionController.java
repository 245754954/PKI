package nudt.web.controller;


import nudt.web.entity.*;
import nudt.web.service.PermissionService;
import nudt.web.service.RolePermissionService;
import nudt.web.service.ServicePermissionService;
import nudt.web.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/per")
public class PermissionController {

    @Autowired
    PermissionService permissionService;

    @Autowired
    RolePermissionService rolePermissionService;

    @Autowired
    ServiceService serviceService;

    @Autowired
    ServicePermissionService servicePermissionService;


    @RequestMapping(value = "/permission",method = {RequestMethod.POST,RequestMethod.GET})
    public String toPermissionPage(){

        return "authorization/permission/index";
    }


    @ResponseBody
    @RequestMapping("/loadData")
    public Object loadData() {
        List<Permission> permissions = new ArrayList<Permission>();

//		Permission root = new Permission();
//		root.setName("顶级节点");
//
//		Permission child = new Permission();
//		child.setName("子节点");
//
//		root.getChildren().add(child);
//		permissions.add(root);

        // 读取树形结构数据
		/*
		Permission root = permissionService.queryRootPermission();

		List<Permission> childPermissions = permissionService.queryChildPermissions(root.getId());

		for ( Permission childPermission : childPermissions ) {
			List<Permission> childChildPermissions = permissionService.queryChildPermissions(childPermission.getId());
			childPermission.setChildren(childChildPermissions);
		}

		root.setChildren(childPermissions);

		permissions.add(root);
		*/

        // 递归查询数据
		/*
		Permission parent = new Permission();
		parent.setId(0);
		queryChildPermissions(parent);
		//permissions.
		return parent.getChildren();
		*/

        // 查询所有的许可数据
        List<Permission> ps = permissionService.findAll();

		/*
		for ( Permission p : ps ) {
			// 子节点
			Permission child = p;
			if ( p.getPid() == 0 ) {
				permissions.add(p);
			} else {
				for ( Permission innerPermission : ps ) {
					if ( child.getPid().equals(innerPermission.getId()) ) {
						// 父节点
						Permission parent = innerPermission;
						// 组合父子节点的关系
						parent.getChildren().add(child);
						break;
					}
				}
			}
		}
		*/

		System.out.println(ps.toString());
        Map<Integer, Permission> permissionMap = new HashMap<Integer, Permission>();
        for (Permission p : ps) {
            permissionMap.put(p.getId(), p);
        }
        for ( Permission p : ps ) {
            Permission child = p;
            if ( child.getPid() == 0 ) {
                permissions.add(p);
            } else {
                Permission parent = permissionMap.get(child.getPid());
                parent.getChildren().add(child);
            }
        }

        return permissions;
    }


    /**
     * 递归查询许可信息
     * 1） 方法自己调用自己
     * 2）方法一定要存在跳出逻辑
     * 3）方法调用时，参数之间应该有规律
     * 4） 递归算法，效率比较低
     * @param parent
     */
    private void queryChildPermissions( Permission parent ) {
        List<Permission> childPermissions = permissionService.findPermissionsByPid(parent.getId());

        for ( Permission permission : childPermissions ) {
            queryChildPermissions(permission);
        }

        parent.setChildren(childPermissions);
    }


    @RequestMapping("/add")
    public String add(Model model,Integer id)
    {
        model.addAttribute("pid",id);

        return "authorization/permission/add";
    }

    @RequestMapping("/edit")
    public String edit( Integer id, Model model ) {

        Permission permission = permissionService.findPermissionById(id);
        model.addAttribute("permission", permission);

        return "authorization/permission/edit";
    }

    @ResponseBody
    @RequestMapping("/insert")
    public Object insert( Permission permission ) {
        AJAXResult result = new AJAXResult();
        Permission p = permissionService.findPermissionByName(permission.getName());
        if(null!=p)
        {
            result.setSuccess(false);
            return result;
        }

        try {
            permissionService.addPermission(permission);
            result.setSuccess(true);
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/update")
    public Object update( Permission permission ) {
        AJAXResult result = new AJAXResult();

        try {
            permissionService.addPermission(permission);
            result.setSuccess(true);
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/delete")
    public Object delete( Permission permission ) {
        AJAXResult result = new AJAXResult();

        try {
            permissionService.deletePermissionById(permission.getId());
            result.setSuccess(true);
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;
    }


    @ResponseBody
    @RequestMapping("/loadAssignData")
    public Object loadAssignData( Integer roleid ) {
        List<Permission> permissions = new ArrayList<Permission>();
        List<Permission> ps = permissionService.findAll();

        // 获取当前角色已经分配的许可信息
        List<RolePermission> rolePermissions =rolePermissionService.findAllByRid(roleid);
        List<Integer> permissionids = new ArrayList<>();

        for(RolePermission r:rolePermissions)
        {
            permissionids.add(r.getPid());
        }
        Map<Integer, Permission> permissionMap = new HashMap<Integer, Permission>();
        for (Permission p : ps) {
            if ( permissionids.contains(p.getId()) ) {
                p.setChecked(true);
            } else {
                p.setChecked(false);
            }
            permissionMap.put(p.getId(), p);
        }
        for ( Permission p : ps ) {
            Permission child = p;
            if ( child.getPid() == 0 ) {
                permissions.add(p);
            } else {
                Permission parent = permissionMap.get(child.getPid());
                parent.getChildren().add(child);
            }
        }

        return permissions;
    }

    @ResponseBody
    @RequestMapping(value = "/isExistPermission",method = {RequestMethod.POST,RequestMethod.GET})
    public Map isExistPermission(@RequestParam("pname")String pname)
    {
            Map returnMap = new HashMap();

            Permission p = permissionService.findPermissionByName(pname);
            if(p!=null)
            {
                returnMap.put("resultCode", 010);
                returnMap.put("msg", "权限已经存在");
            }
            else
            {
                returnMap.put("resultCode", 000);
                returnMap.put("msg", "权限不存在");
            }
            return returnMap;
    }

    //创建一个权限的相关信息

    @RequestMapping(value = "/createInfo",method = {RequestMethod.POST,RequestMethod.GET})
    public String  createInfo(HttpSession session, @RequestParam("pcode") Integer pcode,@RequestParam("name")String name,@RequestParam("url")String url)
    {


        String serviceName = (String) session.getAttribute("serviceName");

        Service service =serviceService.findServiceByServiceName(serviceName);

        Permission permission = new Permission();

        permission.setPid(pcode);
        permission.setUrl(url);
        permission.setName(name);
        permission.setOpen(true);
        permission.setChecked(true);
        permission.setIcon("glyphicon glyphicon-user");
        permission = permissionService.save(permission);

        //保存权限和业务系统之间的关系
        ServicePermission sp = new ServicePermission();
        sp.setServiceId(service.getSid());
        sp.setPermissionId(permission.getId());

        servicePermissionService.save(sp);
        return "redirect:/ser/toAddMenuPage";
    }




}
