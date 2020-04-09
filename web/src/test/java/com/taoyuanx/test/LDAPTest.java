package com.taoyuanx.test;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import nudt.web.AppServerApplication;
import nudt.web.dto.CertReq;
import nudt.web.dto.CertResult;
import nudt.web.entity.Person;
import nudt.web.service.CertService;
import nudt.web.util.ContactAttributeMapperJSON;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.ldap.core.*;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.test.context.junit4.SpringRunner;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import java.util.Date;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplication.class)
public class LDAPTest {

    String BASE_DN="ou=xianyu";

    @Autowired
    ApplicationContext ioc;



    @Autowired
    LdapTemplate ldapTemplate;

    @Autowired
    CertService certService;

    @Test
    public void testConnect(){

        Boolean b = ioc.containsBean("ldapTemplate");
        System.out.println("contain ldapTemplat bean "+b);


    }

    @Test
    public void testQuery(){
        //get users
        AndFilter filter =new AndFilter();
        filter.and(new EqualsFilter("objectclass","Person"));
        List<String> lists =ldapTemplate.search("",filter.encode(),new ContactAttributeMapperJSON());
        for(int i=0;i<lists.size();i++)
        {
            System.out.println(lists.get(i));

        }



        //get group

        lists = ldapTemplate.search("","(&(ou=alipay,dc=zfc,dc=com))",new ContactAttributeMapperJSON());
        System.out.println(lists.toString());
    }

    @Test
    public void query1(){

        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectClass", "person"));
        filter.and(new EqualsFilter("cn","Aiden Zhang"));

        List<String> lists = ldapTemplate.search("",filter.encode(),new ContactAttributeMapperJSON());
        System.out.println(lists.toString());
    }

    @Test
    public void query2(){

        String filter = "(&(objectclass=Person))";
        List<String> list = ldapTemplate.search("ou=alipay", filter, new AttributesMapper() {
            @Override
            public Object mapFromAttributes(Attributes attributes) throws NamingException {
                StringBuilder buf = new StringBuilder();
                Attribute a = attributes.get("cn");
                if (a != null)
                {
                    buf.append(a.get());
                }

                a = attributes.get("uid");
                if (a != null)
                {
                    buf.append(a.get());
                }

                return buf.toString();
            }
        });

        System.out.println(list.toString());
       return ;
    }


    @Test
    public void query3(){
       List<String> lists =  ldapTemplate.search(query().where("objectclass").is("person"), new AttributesMapper<String>() {
            public String mapFromAttributes(Attributes attributes)throws NamingException
            {

                NamingEnumeration<String> ids = attributes.getIDs();

                JSONObject json = new JSONObject();

                while (ids.hasMore())
                {
                    String id = ids.next();
                    //System.out.println(id  +"   "+attributes.get(id).get());
                    json.put(id,attributes.get(id).get());
                }
                return json.toString();
            }
        });
       System.out.println(lists.toString());
        return ;

    }


    @Test
    public void query4(){

       List<String> lists= ldapTemplate.search(query().where("objectclass").is("person"), new AttributesMapper<String>() {


            @Override
            public String mapFromAttributes(Attributes attributes) throws NamingException {

               return (String)attributes.get("cn").get();

            }
        });
       System.out.println(lists);
       return ;
    }
    //将查询到的数据返回为一个类
    @Test
    public void query5(){

        List<Person1> lists =ldapTemplate.search(query().where("objectclass").is("person"),new PersonAttributesMapper());
        System.out.println(lists.toString());
        return ;
    }


    //Entries in LDAP are uniquely identified by their
    // distinguished name (DN). If you have the DN of an entry,
    // you can retrieve the entry directly without searching for it.
    // This is called a lookup in Java LDAP. The following example
    // shows how a lookup for a Person object:
    public Person1 findPerson(String dn){

        Person1 person1 = ldapTemplate.lookup(dn,new PersonAttributesMapper());
        return person1;
    }


    //Let’s say that we want to perform a search starting at the base DN
    // dc=261consulting,dc=com, limiting the returned attributes to "cn"
    // and "sn", with the filter (&(objectclass=person)(sn=?)),
    // where we want the ? to be replaced with the value of the
    // parameter lastName. This is how we do it using the
    // LdapQueryBuilder
    @Test
    public void query7(){
        String sn="";
        LdapQuery ldapQuery = query()
                .base("ou=alipay")
                .attributes("sn","cn")
                .where("objectClass").is("person")
                .and("sn").is("Zhang");
       List<String> lists = ldapTemplate.search(ldapQuery,new ContactAttributeMapperJSON());


        System.out.println(lists.toString());
    }









    //perform Add
    @Test
    public void Add() throws Exception {

        //为待添加的数据构建dn
        Name dn = LdapNameBuilder.newInstance()
                .add("ou", "xianyu")
                .add("cn","Aiden Ning")
                .build();
        //构建属性
        Attributes attrs = new BasicAttributes();
        BasicAttribute ocattr = new BasicAttribute("objectclass");
        ocattr.add("top");
        ocattr.add("person");
        attrs.put(ocattr);
        attrs.put("cn", "Aiden Ning");
        attrs.put("sn", "Ning");
        attrs.put("description","good clerk");

        ldapTemplate.bind(dn,null,attrs);

        return ;
    }



    //perform Add,taobao下面再添加一个组织
    @Test
    public void Add2() throws Exception {
//        CertReq req = new CertReq();
//        req.setC("CN");
//        req.setST("HuNan");
//        req.setE("18392025041@163.com");
//        req.setL("HuNan");
//        req.setO("NUDT");
//        req.setOU("NUDT");
//        req.setCN("*.245754954.com");
//        req.setNotAfter(new Date("2018/10/01"));
//        req.setSerialNumber("1");
//        //RSA类型的签名算法
//        req.setCatype(3);
//
//        CertResult certToUser = certService.certToUser(req,1024);
        //为待添加的数据构建dn
        Name dn = LdapNameBuilder.newInstance()
                .add("ou", "taobao")
                .add("ou","Ant Forest")
                .build();
        //构建属性
        Attributes attrs = new BasicAttributes();
        BasicAttribute ocattr = new BasicAttribute("objectclass");
        ocattr.add("top");
        ocattr.add("organizationalUnit");
        attrs.put(ocattr);
        attrs.put("ou", "Ant Forest");
        attrs.put("description","本公司成立于1994");


        ldapTemplate.bind(dn,null,attrs);

        return ;
    }


    //删除掉刚刚添加的一个组织
    @Test
    public void testUnBind(){
        Name dn = LdapNameBuilder.newInstance()
                .add("ou", "taobao")
                .add("ou","Ant Forest")
                .build();

        ldapTemplate.unbind(dn);

    }

    //跟新操作
    @Test
    public void update(){
        Name dn = LdapNameBuilder.newInstance()
                .add("ou", "taobao")
                .add("ou","Ant Forest")
                .build();
        //构建属性
        Attributes attrs = new BasicAttributes();
        BasicAttribute ocattr = new BasicAttribute("objectclass");
        ocattr.add("top");
        ocattr.add("organizationalUnit");
        attrs.put(ocattr);
        attrs.put("ou", "Ant Forest");
        attrs.put("description","本公司成立于1995");

        ldapTemplate.rebind(dn,null,attrs);


        return ;
    }



    //另外的一种查询方式
    @Test
    public void findByPrimaryKey(){

        Name dn = LdapNameBuilder.newInstance()
                .add("ou", "taobao")
                .add("ou","Ant Forest")
                .build();

        Object lookup = ldapTemplate.lookup(dn, new ContextMapper<Object>() {

            @Override
            public Object mapFromContext(Object ctx) throws NamingException {
                StringBuilder buf = new StringBuilder();
                DirContextAdapter context = (DirContextAdapter) ctx;
                String ou = context.getStringAttribute("ou");
                String description = context.getStringAttribute("description");
                buf.append(ou).append(description);
                return buf.toString();
            }
        });

        System.out.println(lookup);

        return ;

    }



    private class PersonAttributesMapper implements AttributesMapper<Person1> {
        public Person1 mapFromAttributes(Attributes attrs) throws NamingException {
            Person1 person = new Person1();
            person.setFullName((String)attrs.get("cn").get());
            person.setLastName((String)attrs.get("sn").get());
            if(null!=attrs.get("description")){
                person.setDescription((String)attrs.get("description").get());
            }

            return person;
        }
    }
    private class Person1{
        private String fullName;
        private String lastName;
        private String description;

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "Person1{" +
                    "fullName='" + fullName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }





























}
