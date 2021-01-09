package com.jordanho.authentication.dao;


import com.jordanho.authentication.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User, Integer> {
}
