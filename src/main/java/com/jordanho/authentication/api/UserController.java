package com.jordanho.authentication.api;

import com.jordanho.authentication.model.User;
import com.jordanho.authentication.service.EmailService;
import com.jordanho.authentication.service.JWTService;
import com.jordanho.authentication.service.UserService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private EmailService emailService;

    @GetMapping(path="/authorized")
    @CrossOrigin
    public ResponseEntity<?> test1(){
        JSONObject responseBody = new JSONObject();
        responseBody.put("timestamp", new java.util.Date());
        responseBody.put("test", "authorized");
        responseBody.put("path", "/api/user");
        return new ResponseEntity<String>(responseBody.toString(), HttpStatus.OK);
    }

    @GetMapping(path="/unauthorized")
    @CrossOrigin
    public ResponseEntity<?> test2(){
        JSONObject responseBody = new JSONObject();
        responseBody.put("timestamp", new java.util.Date());
        responseBody.put("test", "unauthorized");
        responseBody.put("path", "/api/user");
        emailService.sendEmail();
        return new ResponseEntity<String>(responseBody.toString(), HttpStatus.OK);
    }

    @PostMapping(path="/register")
    @CrossOrigin
    public ResponseEntity<?> register(@RequestBody User user){
        JSONObject responseBody = new JSONObject();
        responseBody.put("timestamp", new java.util.Date());
        responseBody.put("path", "/api/user/register");
        try{
            Integer registerResult = userService.register(user);
            if(registerResult.equals(1)){
                responseBody.put("status", "200");
                responseBody.put("result", "Success");
                responseBody.put("message", "Success");
                HttpHeaders headers = new HttpHeaders();
                headers.add("Set-Cookie","JWT="+jwtService.generateJWT(user)+";");

                return ResponseEntity.status(HttpStatus.OK).headers(headers).body(responseBody);
                //return new ResponseEntity<String>(responseBody.toString(), HttpStatus.OK);
            }else{
                responseBody.put("status", "409");
                responseBody.put("result", "The email inputted have already exist");
                responseBody.put("message", "No message available");
                return new ResponseEntity<String>(responseBody.toString(), HttpStatus.CONFLICT);
            }
        }catch (Exception e){
            responseBody.put("status", "500");
            responseBody.put("result", e.toString());
            responseBody.put("message", e.toString());
            return new ResponseEntity<String>(responseBody.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path="/login")
    public ResponseEntity<?> login(@RequestBody User user){
        JSONObject responseBody = new JSONObject();
        responseBody.put("timestamp", new java.util.Date());
        responseBody.put("path", "/api/user/login");
        try{
            Integer loginResult = userService.login(user);
            if(loginResult.equals(1)){

                responseBody.put("status", "200");
                responseBody.put("result", "Success");
                responseBody.put("message", "Success");

                HttpHeaders headers = new HttpHeaders();
                headers.add("Set-Cookie","JWT="+jwtService.generateJWT(user));
                return ResponseEntity.status(HttpStatus.OK).headers(headers).body(responseBody);

            }else{
                responseBody.put("status", "401");
                responseBody.put("result", "The information inputted are incorrect");
                responseBody.put("message", "No message available");
                return new ResponseEntity<String>(responseBody.toString(), HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception e){
            responseBody.put("status", "500");
            responseBody.put("result", e.toString());
            responseBody.put("message", e.toString());
            return new ResponseEntity<String>(responseBody.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
