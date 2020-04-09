package nudt.web.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


import nudt.web.component.LoginHandlerInterceptor;
import nudt.web.ex.RestCustomHandlerExceptionResolver;
import nudt.web.security.TokenInterceptor;
import nudt.web.util.StringToDateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;


/**
 * @author 都市桃源
 *
 */

//@Configuration代表这是一个配置类，配置类一般是对框架或者容器的一些扩展
//本配置类继承了WebMvcConfigurerAdapter类，意味着这个类是对springmvc的扩展
//我们可以在这里边添加拦截器，视图控制器等等等
//@EnableWebMvc//这句话代表我们将全面接管springmvc的配置，所有的配置都将由我们自己配置
//springboot帮我们自动配置的静态资源解析，bean等都将失效，我们一般不使用


@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
	
	@Autowired
    TokenInterceptor tokenInterceptor;



    public static final Logger log=LoggerFactory.getLogger(WebMvcConfig.class);
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //1.需要定义一个convert转换消息的对象;
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        //2.添加fastJson的配置信息，比如：是否要格式化返回的json数据;
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        //3处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastJsonConfig.setCharset(Charset.forName("UTF-8"));
        //4.在convert中添加配置信息.
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        //5.将convert添加到converters当中.
        converters.add(fastJsonHttpMessageConverter);
        super.configureMessageConverters(converters);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**").excludePathPatterns("/block/toPublicTrading","/error","/page");
        super.addInterceptors(registry);
    }
    //统一异常处理
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        RestCustomHandlerExceptionResolver restCustomHandlerExceptionResolver = new RestCustomHandlerExceptionResolver();
        exceptionResolvers.add(restCustomHandlerExceptionResolver);
        super.configureHandlerExceptionResolvers(exceptionResolvers);
    }


//	 //统一异常处理
//	@Override
//    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
//    	RestCustomHandlerExceptionResolver restCustomHandlerExceptionResolver = new RestCustomHandlerExceptionResolver();
//    	exceptionResolvers.add(restCustomHandlerExceptionResolver);
//        super.configureHandlerExceptionResolvers(exceptionResolvers);
//    }



    /*
    //这是我添加的一个试图控制器
    @Override
    public void addViewControllers(ViewControllerRegistry registry)
     {

        //这里代表只要是/clerk/index请求，我们都将这个请求转发到/themplates/clerk/index.html页面中
        registry.addViewController("/clerk/index").setViewName("clerk/index");
        super.addViewControllers(registry);
    }*/



}