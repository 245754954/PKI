package nudt.web.component;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//实现一个我们自己定义的拦截器，我们主要用来进行登陆检查，没有登陆的用户，不能访问后台主页
//注意拦截器写完还一定要加入到容器中，所以我们写好拦截器以后，还要通过配置类加入到容器
public class LoginHandlerInterceptor implements HandlerInterceptor {
    //我们在登录方法里边设置了，只要用户登录的话我们就会给他设置一个session，因此我们对于
    //任何的方法再访问的时候都会检测，是否存在该session，如果不存在就代表该用户没登陆
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
       Object user =  request.getSession().getAttribute("loginUser");
       //没登陆
       if(null==user){
           //没登陆的话就返回登陆页面,我们此处使用的这中页面转发不是重定向，因此还可以通过request带参数到页面
           request.setAttribute("msg","没有登陆，请您登陆！");
           request.getRequestDispatcher("/clerk/index").forward(request,response);
           return false;
       }
       else {
           //登陆了就不拦截，放行请求，就直接返回true
           return true;

       }

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //访问某些特定的拦截地址，我们可以做日志处理
    }
}
