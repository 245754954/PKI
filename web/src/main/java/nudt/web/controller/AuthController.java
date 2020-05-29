package nudt.web.controller;


import nudt.web.entity.Staff;
import nudt.web.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    StaffService staffService;


    //认证用户是否已经注册
    @ResponseBody
    @RequestMapping(value = "/auth_user",method = {RequestMethod.POST,RequestMethod.GET})
    public Object auth(@RequestParam("username") String username,@RequestParam("password")String password)
    {
        HashMap<String,Object> map = new HashMap<>();
        if(null==username||password==null)
        {
            map.put("status",0);
            return map;
        }
        else
        {
            Staff staff = staffService.findStaffByUsername(username);
            if(staff!=null&&staff.getCode()==1)
            {
                map.put("status",1);
                return map;
            }
            else
            {
                map.put("status",0);
                return map;
            }
        }



    }
}
