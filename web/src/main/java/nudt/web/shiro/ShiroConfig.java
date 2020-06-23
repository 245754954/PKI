package nudt.web.shiro;


import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

//shiro的配置类
@Configuration
public class ShiroConfig {


    //1:创建ShiroFilterFactoryBean
    @Bean//蒋这个bean放入spring容器
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager")DefaultWebSecurityManager securityManager)
    {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //这里就可以基于shiro的内置拦截器来实现一些权限的访问控制
        //常见的过滤器有：
        //1：anon：代表匿名访问，无需登录认证
        //2：authc：代表需要认证才可以访问
        //3：user：如果使用了rememberMe，可以直接访问
        //4：perms代表必须授权才可以使用
        //5：该资源必须得到角色权限才可以访问


        /*
         这里演示需要认证才可以访问的资源
         下面的资源访问都需要用户登陆了才可以，因此当用户没登陆时将会默认被跳转到某个指定的页面


         */

        Map<String,String> map = new LinkedHashMap<>();
        //map.put("/update","authc");//这个是单个url特定的拦截
        //我们也可以采用通配符的拦截方式

        //添加放行的url
        map.put("/bus/toLoginPage","anon");
        map.put("/bus/login","anon");
        //添加授权过滤器，当未授权的用户访问时会被自动的添加到某个未被授权的页面
        //map.put("/bus/toqdAPIPage","perms[user:add]");//访问/bus/toqdAPIPage这个url需要授权

        map.put("/bus/toqdAPIPage","perms[/bus/toqdAPIPage]");
        map.put("/bus/toiconfontPage","perms[/bus/toiconfontPage]");
        map.put("/bus/toagent_addPage","perms[/bus/toagent_addPage]");
        map.put("/bus/toagent_listPage","perms[/bus/toagent_listPage]");
        map.put("/bus/togoodsType_listPage","perms[/bus/togoodsType_listPage]");
        map.put("/bus/togoods_listPage","perms[/bus/togoods_listPage]");
        map.put("/bus/togoods_addPage","perms[/bus/togoods_addPage]");
        map.put("/bus/tobase_addPage","perms[/bus/tobase_addPage]");
        map.put("/bus/tobase_listPage","perms[/bus/tobase_listPage]");
        map.put("/bus/tobase_customListPage","perms[/bus/tobase_customListPage]");
        map.put("/bus/tobase_customNewListPage","perms[/bus/tobase_customNewListPage]");
        map.put("/bus/topagesPage","perms[/bus/topagesPage]");
        map.put("/bus/tomaintainPage","perms[/bus/tomaintainPage]");
        map.put("/bus/tooutPrintDataPage","perms[/bus/tooutPrintDataPage]");
        map.put("/bus/toreportForm1Page","perms[/bus/toreportForm1Page]");
        map.put("/bus/tooutDataPage","perms[/bus/tooutDataPage]");
        map.put("/bus/tointoDataPage","perms[/bus/tointoDataPage]");
        map.put("/bus/tooutPrintDataPage1","perms[/bus/tooutPrintDataPage1]");



        map.put("/bus/*","authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        //如果没有登陆，需要使用这个url跳转到某个页面
        shiroFilterFactoryBean.setLoginUrl("/bus/toLoginPage");
        //
        shiroFilterFactoryBean.setUnauthorizedUrl("/bus/to401Page");
        return shiroFilterFactoryBean;

    }




    //2：创建WebDefaultSecurityManager
    //@Qualifier("staffRealm")该注解会自动到spring容器寻找有没有叫做staffRealm的bean，有的话就自动赋值给参数
    @Bean(name = "securityManager")//bean注解会自动的向容器里放一个名为securityManager的bean，类型为DefaultWebSecurityManager
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("staffRealm") StaffRealm staffRealm)
    {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //securityManager关联realm
        securityManager.setRealm(staffRealm);

        return securityManager;
    }


    //3：创建Realm对象
    @Bean(name="staffRealm")//将方法返回的对象放入spring容器，bean的名字就叫staffRealm
    public StaffRealm getStaffRealm()
    {
        return new StaffRealm();
    }
}
