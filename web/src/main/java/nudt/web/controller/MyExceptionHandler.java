package nudt.web.controller;


import nudt.web.anno.UserNotExistException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

//这里我们自己定义一个异常处理器，一旦我们检测到指定的异常，我们将向前端发送指定的数据，或者进行特定处理
//自定义异常处理器我们需要使用到两个注解

@ControllerAdvice//代表这是一个自定义的异常处理器
public class MyExceptionHandler {

    //捕获全局异常，处理所有不可知的异常
    @ExceptionHandler(value=Exception.class)
    @ResponseBody
    public Object handleException(Exception e, HttpServletRequest request) {

        Map<String, Object> map = new HashMap<>();
        map.put("code", 100);
        map.put("msg", e.getMessage());
        map.put("url", request.getRequestURL());
        return map;
    }

    //自定义异常
    //需要添加thymeleaf依赖
    //路径：src/main/resources/templates/error.html
    @ExceptionHandler(value=UserNotExistException.class)
    @ResponseBody
    public Map<String, Object> handleMyException(UserNotExistException e, HttpServletRequest request) {
        //返回Json数据，由前端进行界面跳转

        Map<String, Object> map = new HashMap<>();
        map.put("code", 100);
        map.put("msg", e.getMsg());
        map.put("url", "http://127.0.0.1:8081/clerk/hello");
        return map;

        //进行页面跳转
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("error.html");
//        modelAndView.addObject("msg", e.getMsg());
//        return modelAndView;
    }
}
