package nudt.web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


//本controller演示如何使用jdbcTemplate进行数据的查询操作
//但是开发的时候我们很少使用jdbcTemplate数据源操作数据库
//我们一般使用druid数据源操作数据库，druid数据源时阿里巴巴的数据源
@Controller
public class JdbcTemplateController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @ResponseBody
    @GetMapping(value = "/query")
    public Map<String,Object> map(){

        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from department");
        //获取第一条数据
        return maps.get(0);
    }
}


