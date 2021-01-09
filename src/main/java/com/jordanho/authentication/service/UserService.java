package com.jordanho.authentication.service;

import com.jordanho.authentication.dao.UserDao;
import com.jordanho.authentication.model.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.security.Key;

import java.util.Calendar;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public Iterable<User> getAllUser(){
        return userDao.findAll();
    }

    public Optional<User> findUserByEmail(String email){
        Iterable<User> userIterable = getAllUser();
        for(User user: userIterable){
            if(user.getEmail().equals(email)){
                return Optional.of(user);
            }
        }
        Optional<User> emptyUser = Optional.empty();
        return emptyUser;
    }

    public String hashPassword(String password){
        return bCryptPasswordEncoder.encode(password);
    }

    public Boolean matchesPasswords(String hashedPassword1, String hashedPassword2){
        return bCryptPasswordEncoder.matches(hashedPassword1, hashedPassword2);
    }

    //public Integer verifyJWT(){

    //}

/*    public String generateJWT(User user){
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        Claims claims = Jwts.claims();
        claims.setIssuer("covid-tracker");
        claims.setSubject(user.getEmail());

        Calendar calendar = Calendar.getInstance();
        claims.setIssuedAt(calendar.getTime());
        calendar.add(Calendar.MINUTE, 2);
        claims.setExpiration(calendar.getTime());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(key)
                .compact();
    }

    public Map<String, Object> parseToken(String token){
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
        Claims claims = parser
                .parseClaimsJws(token)
                .getBody();
        return claims.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }*/



    public int register(User user){
        Optional<User> userMaybe = findUserByEmail(user.getEmail());
        if(userMaybe.isPresent()){
            return 0;
        }else{
            user.setPassword(hashPassword(user.getPassword()));
            user.setUserValidated(false);
            userDao.save(user);
            return 1;
        }
    }

    public int login(User user){
        Optional<User> userMaybe = findUserByEmail(user.getEmail());
        if(userMaybe.isPresent()&&matchesPasswords(user.getPassword(), userMaybe.get().getPassword())){
            return 1;
        }
        return 0;
    }



}
