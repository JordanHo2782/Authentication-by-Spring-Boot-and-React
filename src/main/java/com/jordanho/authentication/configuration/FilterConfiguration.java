package com.jordanho.authentication.configuration;

import com.jordanho.authentication.component.AuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authFilter(){
        FilterRegistrationBean<AuthenticationFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(customAuthenticationFilterCreator());
        registration.addUrlPatterns("/api/user/authorized");
        return registration;
    }

    @Bean
    public AuthenticationFilter customAuthenticationFilterCreator(){
        return new AuthenticationFilter();
    }

}
