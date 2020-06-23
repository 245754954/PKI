package nudt.web.controller;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/bus")
public class BusinessController {





    //登录请求
    @RequestMapping(value = "/login",method = {RequestMethod.POST,RequestMethod.GET})
    public String login(Model model, @RequestParam("name") String name, @RequestParam("password") String password)
    {
        //此处的登录采用了shiro，因此需要使用shiro记录一下用户登录信息
        //1：得到一个subject
        Subject subject = SecurityUtils.getSubject();
        //2:封装用户数据

        UsernamePasswordToken token = new UsernamePasswordToken(name,password);

        //3:执行登陆方法
        try
        {   subject.login(token);
            //登陆成功,跳转到主页
            return "redirect:/bus/toframePage";


        }catch (UnknownAccountException e)
        {
            //登陆失败
            model.addAttribute("msg","用户名不存在");
            return "business/login";
        }catch (IncorrectCredentialsException e)
        {
            model.addAttribute("msg","密码不正确");
            return "business/login";
        }
    }


    @RequestMapping(value = "/logout",method = {RequestMethod.POST,RequestMethod.GET})
    public String logout()
    {

        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "business/login";
    }


    @RequestMapping(value = "/to401Page",method = {RequestMethod.POST,RequestMethod.GET})
    public String to401Page()
    {
        return "business/401";
    }

    //到用户系统的主页
    @RequestMapping(value = "/toframePage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toframePage()
    {
        return "business/frame";
    }
    //到用户系统的主页
    @RequestMapping(value = "/toLoginPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toLoginPage()
    {
        return "business/login";
    }
    //到用户系统的主页
    @RequestMapping(value = "/toIndexPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toIndexPage()
    {
        return "business/tgls/index";
    }

    @RequestMapping(value = "/toqdAPIPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toqdAPIPage()
    {
        return "business/tgls/qdAPI";
    }

    @RequestMapping(value = "/toiconfontPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toiconfontPage()
    {
        return "business/tgls/iconfont";
    }

    @RequestMapping(value = "/toagent_addPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toagent_addPage()
    {
        return "business/tgls/agent/agent_add";
    }

    @RequestMapping(value = "/toagent_listPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toagent_listPage()
    {
        return "business/tgls/agent/agent_list";
    }

    @RequestMapping(value = "/togoodsType_listPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String togoodsType_listPage()
    {
        return "business/tgls/goodsManage/goodsType_list";
    }



    @RequestMapping(value = "/togoods_listPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String togoods_listPage()
    {
        return "business/tgls/goodsManage/goods_list";
    }


    @RequestMapping(value = "/togoods_addPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String togoods_addPage()
    {
        return "business/tgls/goodsManage/goods_add";
    }


    @RequestMapping(value = "/tobase_addPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String tobase_addPage()
    {
        return "business/tgls/base/base_add";
    }



    @RequestMapping(value = "/tobase_listPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String tobase_listPage()
    {
        return "business/tgls/base/base_list";
    }


    @RequestMapping(value = "/tobase_customListPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String tobase_customListPage()
    {
        return "business/tgls/base/base_customList";
    }

    @RequestMapping(value = "/tobase_customNewListPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String tobase_customNewListPage()
    {
        return "business/tgls/base/base_customNewList";
    }


    @RequestMapping(value = "/topagesPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String topagesPage()
    {
        return "business/tgls/base/pages";
    }

    @RequestMapping(value = "/tomaintainPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String tomaintainPage()
    {
        return "business/tgls/base/maintain";
    }

    @RequestMapping(value = "/tooutPrintDataPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String tooutPrintDataPage()
    {
        return "business/tgls/print/outPrintData";
    }

    @RequestMapping(value = "/toreportForm1Page",method = {RequestMethod.POST,RequestMethod.GET})
    public String toreportForm1Page()
    {
        return "business/tgls/reportForm/reportForm1";
    }


    @RequestMapping(value = "/tooutDataPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String tooutDataPage()
    {
        return "business/tgls/into_out/outData";
    }


    @RequestMapping(value = "/tointoDataPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String tointoDataPage()
    {
        return "business/tgls/into_out/intoData";
    }

    @RequestMapping(value = "/tooutPrintDataPage1",method = {RequestMethod.POST,RequestMethod.GET})
    public String tooutPrintDataPage1()
    {
        return "business/tgls/into_out/outPrintData";
    }

    @RequestMapping(value = "/tomodify_passwordPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String tomodify_passwordPage1()
    {
        return "business/tgls/modify_password";
    }



}
