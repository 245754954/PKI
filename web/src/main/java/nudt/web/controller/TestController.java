package nudt.web.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/test1")
    public String test1(Map<String,Object> maps,HttpServletRequest request, HttpServletResponse response){

        maps.put("key","<h1>zfc</h1>");
        maps.put("users", Arrays.asList("zhangsan","lisi","wangwu"));
        return "test/test";
}
}
