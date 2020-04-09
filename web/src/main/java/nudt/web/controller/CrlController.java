package nudt.web.controller;


import nudt.web.entity.CRL;
import nudt.web.service.impl.CrlServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/crl")
public class CrlController {

    @Autowired
    CrlServiceImpl crlService;

    @RequestMapping(value = "/toRevokeCertList",method = {RequestMethod.POST,RequestMethod.GET})
    public String toRevokeCertListPage(Map map, HttpServletRequest request, HttpServletResponse response){

        List<CRL> crls = crlService.findAll();
        map.put("crls",crls);
        return "clerk/cert_revoke_list";
    }

    //返回吊销数字证书的列表

    @ResponseBody
    @RequestMapping(value = "/getCrlList",method = {RequestMethod.POST,RequestMethod.GET})
    public Map getCrlList(){
        HashMap map = new HashMap();
        List<CRL> crls = crlService.findAll();
        map.put("crls",crls);
        return map;
    }
}
