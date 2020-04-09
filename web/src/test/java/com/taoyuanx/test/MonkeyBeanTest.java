package com.taoyuanx.test;

import nudt.web.AppServerApplication;
import nudt.web.entity.Monkey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=AppServerApplication.class)
public class MonkeyBeanTest {

    //从容器中注入这个bean
    @Autowired
    Monkey monkey;

    @Test
    public void testMonkey(){
        System.out.println(monkey);
    }


}
