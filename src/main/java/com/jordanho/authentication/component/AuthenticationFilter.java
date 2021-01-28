package com.jordanho.authentication.component;

import com.jordanho.authentication.model.User;
import com.jordanho.authentication.service.JWTService;
import com.jordanho.authentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
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

@Component
public class AuthenticationFilter implements Filter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserService userService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterchain)
        throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        Cookie[] cookies = httpRequest.getCookies();
        Stream<Cookie> stream = Objects.nonNull(cookies)? Arrays.stream(cookies) : Stream.empty();
        String JWTCookieValue = stream.filter(cookie -> "JWT".equals(cookie.getName()))
                .findFirst()
                .orElse(new Cookie("JWT", ""))
                .getValue();


        try{
            Map<String, Object> tokenParsed = jwtService.parseToken(JWTCookieValue);
            Boolean tokenParsedNotNull = JWTCookieValue.length()!=0;
            Boolean issueByUs = tokenParsed.get("iss").equals("covid-tracker");
            Boolean tokenNotExpired = ((Integer) tokenParsed.get("exp"))>(new Date().getTime()/1000);
            String subEmail = (String) tokenParsed.get("sub");
            Optional<User> userMaybe = userService.findUserByEmail(subEmail);
            Boolean userExist = userMaybe.isPresent();
            if(!(tokenParsedNotNull&&issueByUs&&tokenNotExpired&&userExist)){
                httpResponse.sendRedirect("https://lihkg.com/category/1");
            }
        }catch(Exception e){
            httpResponse.sendRedirect("https://lihkg.com/category/1");
        }

        filterchain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterconfig) throws ServletException {}

}
