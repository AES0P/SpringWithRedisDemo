package com.hzero.demo.springwithredis.spring.service;

import com.hzero.demo.springwithredis.spring.model.User;

import java.io.IOException;
import java.util.List;

public interface UserService {

    Boolean saveUser(User user) throws Exception;

    Boolean saveUsers(List<User> users) throws Exception;

    Boolean deleteUserById(int id);

    Boolean updateUser(User user);

    User findUserById(int id) throws IOException;

    List<User> findAll() throws IOException;

    Boolean flushDb();
}
