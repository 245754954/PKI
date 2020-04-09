package nudt.web.controller;


import nudt.web.entity.AJAXResult;
import nudt.web.entity.Permission;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/aut")
public class AuthorizationController {


    @RequestMapping(value = "/toMenuPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toMenuPage(){



        return "authorization/common/menu";
    }


    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        //session.removeAttribute("loginUser");
        session.invalidate();
        return "redirect:/clerk/index";
    }
}
