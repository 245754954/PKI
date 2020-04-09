package com.taoyuanx.test;

import nudt.web.AppServerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplication.class)
public class LoggerTest {
    //日志记录器
    Logger log =LoggerFactory.getLogger(getClass());
    @Test
    public  void testLog(){
    log.trace("this is trace ...");
    log.info("this is info...");
    log.warn("this is warn...");
    log.error("this is error...");
    log.debug("this is  debug...");

    }
}
