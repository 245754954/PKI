package nudt.web.config;

import nudt.web.entity.Permission;
import nudt.web.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class InitDataBaseComponent implements ApplicationRunner {
    //初始化的时候插入菜单按钮
    @Autowired
    PermissionService permissionService;
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {

        Permission p = permissionService.findPermissionByName("权限菜单");
        if(p==null)
        {
            Permission permission = new Permission();
            permission.setChecked(true);
            permission.setName("权限菜单");
            permission.setOpen(true);
            permission.setPid(0);
            permission.setIcon("glyphicon glyphicon-user");
            permissionService.save(permission);
        }



    }
}
