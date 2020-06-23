package nudt.web.controller;


import cn.hutool.core.util.ZipUtil;
import com.alibaba.fastjson.JSONObject;

import nudt.core.exception.CertException;
import nudt.core.openssl.cert.CertUtil;

import nudt.web.anno.NeedToken;
import nudt.web.common.CAConstant;
import nudt.web.common.Result;
import nudt.web.common.ResultBuilder;
import nudt.web.common.ResultCode;
import nudt.web.config.AppConfig;
import nudt.web.config.CaConfig;
import nudt.web.dto.CertReq;
import nudt.web.dto.CertResult;
import nudt.web.dto.KeyPairResult;
import nudt.web.entity.*;
import nudt.web.repository.CertRepository;
import nudt.web.service.*;
import nudt.web.util.FileUtil;
import nudt.web.util.SimpleTokenManager;
import nudt.web.util.Validator;
import nudt.web.util.ZipUtils;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.asn1.crmf.CertRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialException;
import java.io.File;
import java.io.IOException;
import java.net.HttpCookie;
import java.net.URLEncoder;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Controller
@RequestMapping(value = "api")
@NeedToken(isNeed = true)
public class ApiController {


    @Autowired
    SimpleTokenManager tokenManager;
    @Autowired
    CertService certService;
    @Autowired
    AppConfig appConfig;

    @Autowired
    StaffService staffService;

    @Autowired
    CrlService crlService;

    @Autowired
    PermissionService permissionService;

    @Autowired
    RolePermissionService rolePermissionService;

    @Autowired
    StaffRoleService staffRoleService;

    @Autowired
    UserService userService;


    /**
     * 登录验证
     *
     * @param param
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "login", method = {RequestMethod.GET,RequestMethod.POST})
    @NeedToken(isNeed = false)
    public Result<JSONObject> login(Model model, HttpSession session, @RequestBody JSONObject param, HttpServletResponse response) throws Exception {
        String username = param.getString("username");
        String password = param.getString("password");
        Staff staff = staffService.findStaffByUsername(username);

        if(null!=staff&&staff.getUsername().equals(username)&&staff.getPassword().equals(password)&&1==staff.getCode()){

            //create a token
            String token = tokenManager.createToken(new HashMap<String, String>(), 30L, TimeUnit.MINUTES);

            JSONObject result = new JSONObject();
            //设置用户的cookie
            Cookie cookie=new Cookie("username",username);
            Cookie cookie1=new Cookie("password",password);
            cookie.setMaxAge(1800);
            cookie1.setMaxAge(1800);
            response.addCookie(cookie);
            response.addCookie(cookie1);

            result.put("token", token);
            result.put("username", username);
            return ResultBuilder.buildOkResult(result);
        }else if(null!=staff&&staff.getUsername().equals(username)&&staff.getPassword().equals(password)&&0==staff.getCode()){
            JSONObject result = new JSONObject();
            Result<JSONObject> res = new Result<JSONObject>();
            res.setMsg("个体资料正在等待RA部门审核，审核通过后方可登录系统");
            res.setStatus(ResultCode.FAIL.code);
            return res;
        }else{
            JSONObject result = new JSONObject();
            Result<JSONObject> res = new Result<JSONObject>();
            res.setMsg("用户名或者密码错误");
            res.setStatus(ResultCode.FAIL.code);
            return res;
        }


    }


    /**
     * 公私钥对生成
     *
     * @param param
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Result<JSONObject> create(@RequestBody JSONObject param) throws Exception {
        String type = param.getString("type");
        String keySize = param.getString("keySize");
        if ((type.equals("1") || type.equals("4")) && keySize == null) {
            keySize = "1024";
            Validator.validator().isMatch(Pattern.compile("^[1024|2048]$"), type, "密钥位数不支持");
        }
        Validator.validator().isMatch(Pattern.compile("^[1|2|3|4]$"), type, "密钥对类型不支持");
        JSONObject result = new JSONObject();
        Integer ks = keySize == null ? null : Integer.parseInt(keySize);
        KeyPairResult createKeyPair = certService.createKeyPair(CAConstant.KeyType.forValue(Integer.parseInt(type)), ks);
        result.put("pub", createKeyPair.getPub_pem());
        result.put("pri", createKeyPair.getPri_pem());
        return ResultBuilder.buildOkResult(result);
    }


    @RequestMapping(value = "/toCreatePage",method = {RequestMethod.GET,RequestMethod.POST})
    public String toCreatePage(Model model, HttpServletRequest request,HttpSession session,  HttpServletResponse response){

        return "cert/create";
    }


    @RequestMapping(value = "/toHeaderPage",method = {RequestMethod.GET,RequestMethod.POST})
    public String toHeaderPage(Model model ,HttpServletRequest request,HttpSession session){
        Cookie[] cookies =  request.getCookies();
        String username ="";
        String passwd ="";
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("username")){
                    username = cookie.getValue();
                }
                if(cookie.getName().equals("password")){
                    passwd = cookie.getValue();
                }
            }
        }
        //根据用户名查询用户角色，根据角色查询用户权限，能够查询到登录用户的全部权限信息
        Staff staff1 = staffService.findStaffByUsername(username);
        Integer sid = staff1.getId();
        //根据sid获取用户角色集合
        List<Integer> rids =staffRoleService.findRidsBySid(sid);
        //根据角色找到权限的集合信息
        List<Integer> pids = rolePermissionService.findPidsByRids(rids);
        //找到用户的所有的权限信息
        List<Permission> permissions = permissionService.findPermissionsByPid(pids);

        //将所有的权限保存到session
        Map<Integer, Permission> permissionMap = new HashMap<Integer, Permission>();

        Permission root = null;

        for ( Permission permission : permissions ) {
            //将用户所有的permission都放入map里边
            permissionMap.put(permission.getId(), permission);
        }

        for ( Permission permission : permissions ) {
            //把每一个permission都作为child
            Permission child = permission;
            if ( child.getPid() == 0 ) {
                //如果permission的pid为0，那么就代表根节点
                root = permission;
            } else {
                //否则就作为子节点
                Permission parent = permissionMap.get(child.getPid());
                //将子节点加入父节点
                parent.getChildren().add(child);
            }
        }

        session.setAttribute("rootPermission", root);
        model.addAttribute("rootPermission",root);

        return "cert/header";
    }

    @RequestMapping(value = "/toCertPage",method = {RequestMethod.GET,RequestMethod.POST})
    public String toCertPage(HashMap<String,Object> map,HttpServletRequest request){
        Cookie[] cookies =  request.getCookies();
        String username ="";
        String passwd ="";
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("username")){
                    username = cookie.getValue();
                }
                if(cookie.getName().equals("password")){
                    passwd = cookie.getValue();
                }
            }
        }
       Staff staff =  staffService.findStaffByUsername(username);
       if(null!=staff){
            map.put("staff",staff);
       }

        return "cert/cert";
    }



    @RequestMapping(value = "/toLoginPage",method = {RequestMethod.GET,RequestMethod.POST})
    @NeedToken(isNeed = false)
    public String toLoginPage(){

        return "cert/login";
    }
    /**
     * 公私钥对生成
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "cert", method = RequestMethod.POST)
    public Result<JSONObject> cert(@RequestBody CertReq certReq,HttpServletRequest request) throws Exception {
        Integer keySize = certReq.getKeySize() == null ? null : Integer.parseInt(certReq.getKeySize());
        String serialNumber = String.valueOf(System.currentTimeMillis());
        Cookie[] cookies =  request.getCookies();
        String username ="";
        String passwd ="";
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("username")){
                     username = cookie.getValue();
                }
                if(cookie.getName().equals("password")){
                     passwd = cookie.getValue();
                }
            }
        }
        Cert cert = certService.findCertByUsername(username);
        if(null!=cert)
        {
                JSONObject result = new JSONObject();
                result.put("serialNumber",cert.getSerialNumber());

            return ResultBuilder.buildFailResult(result);

        }
        certReq.setSerialNumber(serialNumber);
        //为用户生成公钥私钥，并创建证书
        CertResult certToUser = certService.certToUser(certReq, keySize);

        CaConfig caConfig = appConfig.getByKeyType(certReq.getCatype());
        String certDir = caConfig.getClientCertBasePath() + "/" + serialNumber;
        String pri_pem_path = certDir + "/"+username+"_pri.pem";
        String pub_pem_path = certDir + "/"+username+"_pub.pem";
        String cert_path = certDir + "/"+username+"_cert.cer";
        String p12_path = certDir + "/"+username+".p12";
        //用户的别名
        String userAlias = username;
        String password = passwd;

        FileUtils.forceMkdir(new File(certDir));
        CertUtil.savePrivateKeyPem(certToUser.getPri(), pri_pem_path);
        CertUtil.savePublicKeyPem(certToUser.getPub(), pub_pem_path);
        CertUtil.saveX509CertBase64(certToUser.getCert(), cert_path);
        CertUtil.savePKCS12(certToUser.getCert(), certToUser.getPri(), userAlias, password, p12_path);
        JSONObject result = new JSONObject();
        result.put("cert_path", cert_path);
        result.put("userAlias", userAlias);
        result.put("p12_path", p12_path);
        result.put("password", password);
        result.put("pri", FileUtils.readFileToString(new File(pri_pem_path)));
        result.put("pub", FileUtils.readFileToString(new File(pub_pem_path)));
        //根据证书的编号下载检索证书
        result.put("serialNumber", serialNumber);
        result.put("catype", certReq.getCatype());

        //用户的某个证书的相关信息
        Cert c = new Cert();
        c.setSerialNumber(serialNumber);
        c.setUsername(username);
        c.setVersion(certToUser.getCert().getVersion());
        c.setIssuerDN(certToUser.getCert().getIssuerDN().toString());
        c.setSignatureAlgorithm(certToUser.getCert().getSigAlgName());
        Integer type = certReq.getCatype();
        if(type==1)
        {
            c.setAlgorithm("RSA");
        }else if(type==2){
            c.setAlgorithm("SM2");
        }else if(type==3)
        {
            c.setAlgorithm("ECDSA");
        }else if(type==4)
        {
            c.setAlgorithm("DSA");
        }
        c.setEncryptpassword(certReq.getEncryptpassword());
        c.setKeysize(keySize);
        c.setCaType(certReq.getCatype());
        c.setSigAlg(certReq.getSignAlg());
        c.setCertDir(certDir);
        c.setPub_pem_path(pub_pem_path);
        c.setPri_pem_path(pri_pem_path);
        c.setP12_path(p12_path);
        c.setUserAlias(userAlias);
        c.setPassword(password);
        c.setNotBefore(certToUser.getCert().getNotBefore());
        c.setNotAfter(certToUser.getCert().getNotAfter());
        c.setStatus("validity");

        //设置证书的状态
        certService.save(c);


        //将证书的信息存储到apacheDS LDAP服务器
        Staff staff = staffService.findStaffByUsername(username);
        Name dn = LdapNameBuilder.newInstance()
                .add("o","CA")
                .add("ou", staff.getOrganization())
                .build();

         AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectClass", "person"));
        filter.and(new EqualsFilter("cn",staff.getUsername()));
        List<User> users = userService.search(dn.toString(),filter.encode());
        //用户的证书信息没保存的话在进行保存，否则不保存
        if(users.size()==0)
        {

            byte[] publicKey = CertUtil.readPublicKeyPem (pub_pem_path).getEncoded();
            byte[] privateKey = CertUtil.readPrivateKeyPem(pri_pem_path).getEncoded();
            byte[] p12 = CertUtil.readKeyStore(p12_path,"123456").getKey(userAlias,"123456".toCharArray()).getEncoded();
            byte[] cert_byte = CertUtil.readX509Cert(cert_path).getEncoded();
            userService.create(dn,staff.getUsername(),staff.getEmail(),staff.getUsername(),c.getAlgorithm(),p12,publicKey,privateKey,cert_byte);
        }

        return ResultBuilder.buildOkResult(result);
    }
    @ResponseBody
    @RequestMapping(value = "downCertZip", method = {RequestMethod.POST,RequestMethod.GET})
    public void downCertZip( HttpServletResponse resp, HttpServletRequest req) throws SerialException {
        try {
            Cookie[] cookies =  req.getCookies();
            String username ="";
            String passwd ="";
            if(cookies != null){
                for(Cookie cookie : cookies){
                    if(cookie.getName().equals("username")){
                        username = cookie.getValue();
                    }
                    if(cookie.getName().equals("password")){
                        passwd = cookie.getValue();
                    }
                }
            }
            //下载证书的时候需要根据用户的用户名字来确定用户的证书
            //因为本系统下一个用户只对应了一个证书
            Cert cert = certService.findCertByUsername(username);
            CAConstant.KeyType keyType = CAConstant.KeyType.forValue(cert.getCaType());
            CaConfig caConfig = appConfig.getByKeyType(cert.getCaType());
            String certDir = caConfig.getClientCertBasePath() + "/" + cert.getSerialNumber();
            String destPath = caConfig.getClientCertBasePath() + "/" + UUID.randomUUID().toString() + ".zip";
            String readmeDoc = certDir + "/readme.txt";
            String readmeTxt = "证书类型为["+keyType.name+"]p12 alias:"+username+"\r\n p12 密码:"+passwd;
            FileUtils.writeStringToFile(new File(readmeDoc), readmeTxt, "UTF-8");
            //对文件夹压缩加密

            ZipUtils.compressFolder(destPath,cert.getEncryptpassword(),certDir);
            // 下载
            File destZipFile = new File(destPath);
            resp.setContentType(req.getServletContext().getMimeType(destZipFile.getName()));
            resp.setHeader("Content-type", "application/octet-stream");
            resp.setHeader("Content-Disposition",
                    "attachment;fileName=" + URLEncoder.encode(keyType.name+"_cert.zip","UTF-8"));


            resp.getOutputStream().write(FileUtils.readFileToByteArray(destZipFile));


        } catch (Exception e) {
            throw new SerialException("下载失败");
        }
    }

    //证书冻结操作

    @RequestMapping(value = "/toCertfrozePage",method = {RequestMethod.POST,RequestMethod.GET})
    public String certFroze(Map map,HttpServletRequest request, HttpServletResponse response) throws CertException, CertificateNotYetValidException, CertificateExpiredException {

        Cookie[] cookies =  request.getCookies();
        String username ="";
        String passwd ="";
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("username")){
                    username = cookie.getValue();
                }
                if(cookie.getName().equals("password")){
                    passwd = cookie.getValue();
                }
            }
        }
        Cert cert = certService.findCertByUsername(username);
        //根据数字证书的serialNumbner查询数字证书的有效性，然后设置cert数据库表的status这状态
        CAConstant.KeyType keyType = CAConstant.KeyType.forValue(cert.getCaType());
        CaConfig caConfig = appConfig.getByKeyType(cert.getCaType());
        String certDir = caConfig.getClientCertBasePath() + "/" + cert.getSerialNumber();
        String certPath = certDir + "/"+cert.getUsername()+"_cert.cer";
        X509Certificate userCert = CertUtil.readX509Cert(certPath);
        //证书的实际状态
        String status = CertUtil.judgeValidity(userCert);
        //设置证书的实际状态
        cert.setStatus(status);
        //同时每次用户查询都会修改数据库的证书状态
        certService.save(cert);

        map.put("cert",cert);
        return "cert/froze" ;

    }
    //证书冻结操作，其实就是将数字证书的开始日期向后面顺延，但是不能超过总的日期
    @RequestMapping(value = "/certFroze",method = {RequestMethod.POST,RequestMethod.GET})
    public String certFroze(Map map,Integer days,Cert cert,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Cookie[] cookies =  request.getCookies();
        String username ="";
        String passwd ="";
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("username")){
                    username = cookie.getValue();
                }
                if(cookie.getName().equals("password")){
                    passwd = cookie.getValue();
                }
            }
        }

        int day = (int)((cert.getNotAfter().getTime()-cert.getNotBefore().getTime())/(3600*1000*24));
        if(days>=day)
        {   Cert cert1 = certService.findCertByUsername(username);
            map.put("msg","冻结时间输入不合法");
            map.put("cert",cert1);
            return "/cert/froze";
        }

        String username1 = cert.getUsername();
        Cert userCertInfo = certService.findCertByUsername(username1);
        Staff staff = staffService.findStaffByUsername(username1);//修改证书的有效时间
        CertReq certReq = new CertReq();
        certReq.setSerialNumber(userCertInfo.getSerialNumber());
        certReq.setC(staff.getCountrycode());
        certReq.setE(staff.getEmail());
        certReq.setCN(staff.getUsername());
        certReq.setCatype(userCertInfo.getCaType());
        certReq.setL(staff.getCity());
        certReq.setO(staff.getOrganization());
        certReq.setOU(staff.getDepartment());
        certReq.setST(staff.getProvince());
        certReq.setSignAlg(userCertInfo.getSigAlg());
        certReq.setKeySize(userCertInfo.getKeysize()+"");
        //把证书的有效时间推移，也就是相当于冻结数字证书
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        Date begin_date =new Date();
        Date end_date = cert.getNotAfter();

        Calendar c1 = Calendar.getInstance();
        c1.setTime(begin_date);
        c1.add(Calendar.DAY_OF_MONTH, days);
        begin_date =c1.getTime();

        String b = sdf.format(begin_date);
        String e = sdf.format(end_date);

        begin_date = sdf.parse(b);
        end_date = sdf.parse(e);

        certReq.setNotBefore(begin_date);
        certReq.setNotAfter(end_date);

        //为用户生成公钥私钥，并创建证书
        CertResult certToUser = certService.certToUser(certReq, cert.getKeysize());

        CaConfig caConfig = appConfig.getByKeyType(certReq.getCatype());
        String certDir = caConfig.getClientCertBasePath() + "/" + cert.getSerialNumber();
        String pri_pem_path = certDir + "/"+username+"_pri.pem";
        String pub_pem_path = certDir + "/"+username+"_pub.pem";
        String cert_path = certDir + "/"+username+"_cert.cer";
        String p12_path = certDir + "/"+username+".p12";
        //用户的别名
        String userAlias = username;
        String password = passwd;

        FileUtils.forceMkdir(new File(certDir));
        CertUtil.savePrivateKeyPem(certToUser.getPri(), pri_pem_path);
        CertUtil.savePublicKeyPem(certToUser.getPub(), pub_pem_path);
        CertUtil.saveX509CertBase64(certToUser.getCert(), cert_path);
        CertUtil.savePKCS12(certToUser.getCert(), certToUser.getPri(), userAlias, password, p12_path);
        JSONObject result = new JSONObject();
        result.put("cert_path", cert_path);
        result.put("userAlias", userAlias);
        result.put("p12_path", p12_path);
        result.put("password", password);
        result.put("pri", FileUtils.readFileToString(new File(pri_pem_path)));
        result.put("pub", FileUtils.readFileToString(new File(pub_pem_path)));
        //根据证书的编号下载检索证书
        result.put("serialNumber", cert.getSerialNumber());
        result.put("catype", certReq.getCatype());

        //用户的某个证书的相关信息
        Cert c = new Cert();
        c.setSerialNumber(cert.getSerialNumber());
        c.setUsername(username);
        c.setVersion(certToUser.getCert().getVersion());
        c.setIssuerDN(certToUser.getCert().getIssuerDN().toString());
        c.setSignatureAlgorithm(certToUser.getCert().getSigAlgName());
        Integer type = certReq.getCatype();
        if(type==1)
        {
            c.setAlgorithm("RSA");
        }else if(type==2){
            c.setAlgorithm("SM2");
        }else if(type==3)
        {
            c.setAlgorithm("ECDSA");
        }else if(type==4)
        {
            c.setAlgorithm("DSA");
        }
        c.setKeysize(userCertInfo.getKeysize());
        c.setCaType(certReq.getCatype());
        c.setSigAlg(certReq.getSignAlg());
        c.setCertDir(certDir);
        c.setPub_pem_path(pub_pem_path);
        c.setPri_pem_path(pri_pem_path);
        c.setP12_path(p12_path);
        c.setUserAlias(userAlias);
        c.setPassword(password);
        c.setNotBefore(certToUser.getCert().getNotBefore());
        c.setNotAfter(certToUser.getCert().getNotAfter());
        c.setStatus("valid");
        //设置证书的状态
        certService.save(c);

        return "redirect:/api/toCertfrozePage";
    }


    //到证书信息页面
    @RequestMapping(value = "/toCertStatusPage",method = {RequestMethod.POST,RequestMethod.GET})
    public String toCertStatusPage(Map map,HttpServletRequest request,HttpServletResponse response){

        Cookie[] cookies =  request.getCookies();
        String username ="";
        String passwd ="";
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("username")){
                    username = cookie.getValue();
                }
                if(cookie.getName().equals("password")){
                    passwd = cookie.getValue();
                }
            }
        }

        Cert cert = certService.findCertByUsername(username);
        Staff staff = staffService.findStaffByUsername(username);
        //查询到所有应撤销的数字证书
        List<CRL> crls = crlService.findAll();
        map.put("cert",cert);
        map.put("staff",staff);
        map.put("crls",crls);


        return "cert/cert_status";
    }

    //撤销用户的数字证书
    @RequestMapping(value = "/certRevoke" ,method = {RequestMethod.POST,RequestMethod.GET})
    public String revokeCert(HttpServletResponse response,HttpServletRequest request) throws CertException, InvalidNameException {
        //删除用户的数字证书信息
        Cookie[] cookies =  request.getCookies();
        String username ="";
        String passwd ="";
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("username")){
                    username = cookie.getValue();
                }
                if(cookie.getName().equals("password")){
                    passwd = cookie.getValue();
                }
            }
        }
        Cert cert = certService.findCertByUsername(username);
        String serialNumber = cert.getSerialNumber();

        //删除已经存在的证书信息，并保存到CRL
        CaConfig caConfig = appConfig.getByKeyType(cert.getCaType());
        String certDir = caConfig.getClientCertBasePath() + "/" + cert.getSerialNumber();
        String cert_path = certDir + "/"+username+"_cert.cer";
        String pri_pem_path = certDir + "/"+username+"_pri.pem";
        String pub_pem_path = certDir + "/"+username+"_pub.pem";
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        File file = new File(cert_path);
        // 毫秒数
        long modifiedTime = file.lastModified();
        // 通过毫秒数构造日期 即可将毫秒数转换为日期
        Date d = new Date(modifiedTime);
        PublicKey publicKey = CertUtil.readPublicKeyPem(pub_pem_path);
        PrivateKey privateKey = CertUtil.readPrivateKeyPem(pri_pem_path);
        X509Certificate certificate = CertUtil.readX509Cert(cert_path);
        CRL crl = new CRL();
        crl.setCreateTime(d);
        crl.setIsValidate(false);
        crl.setPriKey(privateKey.toString());
        crl.setPubKey(publicKey.toString());
        crl.setSerialNumber(certificate.getSerialNumber().toString());
        crlService.Add(crl);

        //删除保存数字证书目录下的数字证书及其文件夹
        FileUtil.delFile(certDir);



        //将已经删除的证书加入到作为CRL列表，提供给他人查询
        //删除数据库的记录
        certService.deleteBySerialNumber(serialNumber);


        //将LDAP数据库中的信息也删除
        //将证书的信息存储到apacheDS LDAP服务器
        Staff staff = staffService.findStaffByUsername(username);
        Name dn = LdapNameBuilder.newInstance()
                .add("o","CA")
                .add("ou", staff.getOrganization())
                .build();
        dn.add("cn="+staff.getUsername());
        userService.delete(dn);

        return "redirect:/api/toCertStatusPage";
    }




    @RequestMapping(value = "/toPersonalCenter",method = {RequestMethod.POST,RequestMethod.GET})
    public String toPersonalCenter(Model model,HttpSession session,Map map,HttpServletRequest request,HttpServletResponse response ){
        Cookie[] cookies =  request.getCookies();
        String username ="";
        String passwd ="";
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("username")){
                    username = cookie.getValue();
                }
                if(cookie.getName().equals("password")){
                    passwd = cookie.getValue();
                }
            }
        }
        //根据用户名查询用户角色，根据角色查询用户权限，能够查询到登录用户的全部权限信息
        Staff staff1 = staffService.findStaffByUsername(username);
        if(staff1!=null)
        {
            Integer sid = staff1.getId();
            //根据sid获取用户角色集合
            List<Integer> rids =staffRoleService.findRidsBySid(sid);
            //根据角色找到权限的集合信息
            List<Integer> pids = rolePermissionService.findPidsByRids(rids);
            //找到用户的所有的权限信息
            List<Permission> permissions = permissionService.findPermissionsByPid(pids);

            //将所有的权限保存到session
            Map<Integer, Permission> permissionMap = new HashMap<Integer, Permission>();

            Permission root = null;

            for ( Permission permission : permissions ) {
                //将用户所有的permission都放入map里边
                permissionMap.put(permission.getId(), permission);
            }

            for ( Permission permission : permissions ) {
                //把每一个permission都作为child
                Permission child = permission;
                if ( child.getPid() == 0 ) {
                    //如果permission的pid为0，那么就代表根节点
                    root = permission;
                } else {
                    //否则就作为子节点
                    Permission parent = permissionMap.get(child.getPid());
                    //将子节点加入父节点
                    parent.getChildren().add(child);
                }
            }

            if(root==null)
            {
                root = new Permission();
            }
            model.addAttribute("rootPermission",root);

            Cert cert = certService.findCertByUsername(username);
            Staff staff = staffService.findStaffByUsername(username);

            map.put("cert",cert);
            map.put("staff",staff);
        }


        return "cert/personalcenter";
    }

}
