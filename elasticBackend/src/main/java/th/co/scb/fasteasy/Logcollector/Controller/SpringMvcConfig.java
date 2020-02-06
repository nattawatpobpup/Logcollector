package th.co.scb.fasteasy.Logcollector.Controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
@Controller
public class SpringMvcConfig implements WebMvcConfigurer {

    @RequestMapping(value = "/admin")
    public String admin(){
        return "forward:/";
    }
    @RequestMapping(value = "/user")
    public String user(){
        return "forward:/";
    }

//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/admin").setViewName("admin/index");
//
//    }

}
