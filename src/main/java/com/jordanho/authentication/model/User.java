package com.jordanho.authentication.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("userValidated")
    private Boolean userValidated;

    @JsonProperty("registeredDate")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date registeredDate;

    @JsonProperty("lastModifiedDate")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastModifiedDate;

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getUserValidated() {
        return userValidated;
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserValidated(Boolean userValidated) {
        this.userValidated = userValidated;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

}
