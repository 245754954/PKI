package com.taoyuanx.test;

import java.util.Date;


import nudt.web.AppServerApplication;
import nudt.web.common.CAConstant;
import nudt.web.dto.CertReq;
import nudt.web.dto.CertResult;
import nudt.web.dto.KeyPairResult;
import nudt.web.service.CertService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest(classes=AppServerApplication.class)
public class SpringJunitTest {
	@Autowired
	CertService certService;
	@Test
	public void testCert() throws Exception{
		CertReq req = new CertReq();
		req.setC("CN");
		req.setST("BJ");
		req.setE("lianglei_lzx@163.com");
		req.setL("BJ");
		req.setO("taoyuanx-client");
		req.setOU("taoyuanx-client");
		req.setCN("*.taoyuanx.com");
		req.setNotAfter(new Date("2018/10/01"));
		req.setSerialNumber("1");
		//RSA类型的签名算法
		req.setCatype(3);

		System.out.println(req.getUserDN());
		CertResult certToUser = certService.certToUser(req,1024);

		//System.out.println("this is the certToUser"+certToUser);


		KeyPairResult createKeyPair = certService.createKeyPair(CAConstant.KeyType.RSA, 1024);
		System.out.println(createKeyPair.getPri_pem());
		System.out.println(createKeyPair.getPub_pem());
	}
}
