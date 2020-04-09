package nudt.web.config;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


//实现自己的一个拦截器,记得需要加入到配置类里边，这个相当于自己的一个组件
public class LoginIntecepter implements HandlerInterceptor {
    //在控制器执行之前执行逻辑操作，返回false的话就不会在进入到控制器了
    //我们在这里需要判断某个用户是否登录，可以根据session判断用户是否登录
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        // 判断当前用户是否已经登陆
        HttpSession session = request.getSession();
        String loginUser = (String)session.getAttribute("loginUser");

        if ( loginUser == null ) {
            String path = session.getServletContext().getContextPath();
            response.sendRedirect(path + "/clerk/index");
            return false;
        } else {
            return true;
        }
    }

    //控制器执行完毕之后所进行的逻辑操作，可以进行日志处理
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    //完成试图渲染以后，执行的操作
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
