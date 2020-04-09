package com.taoyuanx.test;

import nudt.web.AppServerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplication.class)
public class ComponentTest {


    //查看容器中是否具有某个bean
    @Autowired
    ApplicationContext ioc;

    @Test
    public void testComponent(){

        Boolean b = ioc.containsBean("helloService");
        System.out.println("contain beans "+b);
    }


}
