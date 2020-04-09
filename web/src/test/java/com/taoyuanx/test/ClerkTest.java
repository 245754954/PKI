
package com.taoyuanx.test;

import nudt.web.AppServerApplication;
import nudt.web.entity.Clerk;

import nudt.web.repository.ClerkRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplication.class)
public class ClerkTest {

    @Autowired
    ClerkRepository clerkRepository;

    @Test
    public void add(){


        Clerk clerk = new Clerk();
        clerk.setFullName("gino");
        clerk.setUserPassword("111111");
        clerk.setLastName("G");
        clerkRepository.save(clerk);


        return ;
    }

    //查询出所有的person对象，并且将值赋值给clerk
    @Test
    public void query(){

        clerkRepository.findAll().forEach(p->{
            System.out.println(p);
        });
    }
}
