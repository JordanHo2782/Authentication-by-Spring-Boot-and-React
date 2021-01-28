package com.jordanho.authentication.service;

import com.jordanho.authentication.dao.UserDao;
import com.jordanho.authentication.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class JWTService {

    @Autowired
    private UserDao userDao;

    private final String secretKeyString = "xJfKpF1tsmvM4rR37th0WYc9sGjFnuqRANwifz3wEAo=";

    public String generateJWT(User user){

        Claims claims = Jwts.claims();
        claims.setIssuer("covid-tracker");
        claims.setSubject(user.getEmail());
        claims.setIssuedAt(new Date());
        claims.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5)));
        //Problem in signWith key

        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString));

        String JWS = Jwts.builder()
                .setClaims(claims)
                .signWith(key)
                .compact();
        return JWS;

    }

    public Map<String, Object> parseToken(String token){

        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString));

        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
        Claims claims = parser
                .parseClaimsJws(token)
                .getBody();
        return claims.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
