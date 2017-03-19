package ch.fhnw.edu.eaf.eventmgmt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by lukasschonbachler on 15.03.17.
 */

@Configuration
public class StaticResourceConfiguration extends WebMvcConfigurerAdapter {

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/file/**").addResourceLocations("file://" + uploadPath);
    }
}