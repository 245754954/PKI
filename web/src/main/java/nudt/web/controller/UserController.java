package nudt.web.controller;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import nudt.web.entity.User;
import nudt.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//一个如何使用JPA模板的例子
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserRepository userRepository;


    @ResponseBody
    @RequestMapping(value = "/getone" ,method = {RequestMethod.GET,RequestMethod.POST})
    public User getUser(@RequestParam("id")Integer id){
        User one = userRepository.findOne(id);

        return  one;
    }

    //返回保存的对象
    @GetMapping(value = "/save")
    public User save(User user){
        //将保存的对象返回来
        User save = userRepository.save(user);
        return save;
    }
}
