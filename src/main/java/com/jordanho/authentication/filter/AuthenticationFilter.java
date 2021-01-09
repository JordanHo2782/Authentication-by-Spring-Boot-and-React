package com.jordanho.authentication.filter;

import com.jordanho.authentication.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationFilter implements Filter {

    @Autowired
    private JWTService jwtService = new JWTService();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterchain)
        throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        //httpResponse.sendRedirect("https://www.youtube.com/");


        Cookie[] cookies = httpRequest.getCookies();
        Stream<Cookie> stream = Objects.nonNull(cookies)? Arrays.stream(cookies) : Stream.empty();
        String JWTCookieValue = stream.filter(cookie -> "JWT".equals(cookie.getName()))
                .findFirst()
                .orElse(new Cookie("JWT", ""))
                .getValue();

        System.out.println("filter");
        if(JWTCookieValue.length()==0){
            System.out.println("No token");
        }else{
            Map<String, Object> tokenParsed = jwtService.parseToken(JWTCookieValue);
            System.out.println(tokenParsed);
        }


        //System.out.println(tokenParsed);
        //Do the authentication here


        filterchain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterconfig) throws ServletException {}

}
