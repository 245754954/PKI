package nudt.web.config;

import nudt.web.service.HelloService;
import nudt.web.util.ImageServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//这里是一个自己的配置类，springboot中，配置类我们使用@configuration注解
//配置类其实就相当于配置文件，配置类用来代替配置文件
@Configuration
public class MyAppConfig {

    //将方法的返回值注入到容器中，容器中这个组件默认的id就是方法的名称
    @Bean
    public HelloService helloService(){
        System.out.println("配置类@bean给容器添加组件了！");
        return  new HelloService();
    }

    //二维码产生的servlet
    @Bean
    public ServletRegistrationBean servletRegistrationBean1() {
        return new ServletRegistrationBean(new ImageServlet(), "/servlet/imgCheckCode");// ServletName默认值为首字母小写，即myServlet
    }
}
