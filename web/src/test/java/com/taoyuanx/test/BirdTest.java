package com.taoyuanx.test;


import nudt.web.AppServerApplication;
import nudt.web.entity.Bird;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplication.class)
public class BirdTest {

    @Autowired
    Bird bird;

    @Test
    public void testBird(){
        System.out.println(bird);
    }
}
