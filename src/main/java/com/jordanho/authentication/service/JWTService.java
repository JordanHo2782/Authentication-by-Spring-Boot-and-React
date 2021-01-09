package com.jordanho.authentication.service;

import com.jordanho.authentication.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class JWTService {

    private SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateJWT(User user){
        Claims claims = Jwts.claims();
        claims.setIssuer("covid-tracker");
        claims.setSubject(user.getEmail());

        claims.setIssuedAt(new Date());
        claims.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5)));
        //Problem in signWith key

        //String secretString = Encoders.BASE64.encode(key.getEncoded());

        String JWS = Jwts.builder()
                .setClaims(claims)
                .signWith(key)
                .compact();

        System.out.println(JWS);

        return JWS;
    }

    public Map<String, Object> parseToken(String token){
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
        Claims claims = parser
                .parseClaimsJws(token)
                .getBody();
        System.out.println(claims);
        return claims.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


}
