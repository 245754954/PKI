package com.taoyuanx.test;

import nudt.web.AppServerApplication;
import nudt.web.entity.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



//这两个注解代表我们将使用springboot提供的方式进行测试，好处是这个测试类中如果我们需要使用
//容器中任何已有的bean，我们只需用使用@autowired注入就好了，如下 ：我们需要使用person这个bean
//我们只需用进行如下主节 @Autowired
//    Person person;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=AppServerApplication.class)
public class PersonBeanTest {

    //从容器中注入这个bean
    @Autowired
    Person person;


    @Test
    public void testPerson(){

        System.out.println(person);
    }
}
