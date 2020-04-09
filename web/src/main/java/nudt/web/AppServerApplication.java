package nudt.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

//这个ImportResoucer注解是用于结合传统的spring注解，例如：springboot中有时候没有我们自己编写的
//beans，而且这个bean配置在xml文件中，我们希望把这个xml文件中的bean加入到springboot容器中
//我们就需要在这里导入一下该xml文件，这个xml文件可以为多个，此处为了演示我们就使用了一个xml文件
//这个beans.xml文件中只有一个service 类型的bean，我们使用ImportResource注解将该bean加入springboot容器
//@ImportResource(locations = {"beans.xml"})//我们现在已经不推荐使用了，我们现在推荐使用配置类
@SpringBootApplication
public class AppServerApplication {
	/**
	 * 文件上传临时路径
	 */
	/* @Bean
	 MultipartConfigElement multipartConfigElement() {
	    MultipartConfigFactory factory = new MultipartConfigFactory();
	    factory.setLocation("e://tmp/");
	    return factory.createMultipartConfig();
	}*/
	
	public static void main(String[] args) {

		SpringApplication.run(AppServerApplication.class, args);
	}



}
