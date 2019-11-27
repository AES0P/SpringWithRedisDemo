package com.hzero.demo.springwithredis.spring.service.impl;

import com.hzero.demo.springwithredis.redis.util.RedisUtils;
import com.hzero.demo.springwithredis.spring.dao.impl.UserDaoImpl;
import com.hzero.demo.springwithredis.spring.model.User;
import com.hzero.demo.springwithredis.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service(value = "userServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    public UserDaoImpl userDao;

    @Autowired
    public RedisUtils redisUtils;

    @Override
//    @CachePut(value = "content", key = "#user.getId()")
    public Boolean saveUser(User user) throws Exception {

        if (userDao.saveUser(user)) {
            redisUtils.sendMessage("aesop", new Date() + ": saveUser:" + user.getId());
            return true;
        }

        return false;
    }

    @Override
    public Boolean saveUsers(List<User> users) throws Exception {

        if (userDao.saveUsers(users)) {

            redisUtils.sendMessage("aesop", new Date() + ": saveUsers");
            return true;
        }

        return false;
    }

    @Override
//    @CacheEvict(value = "content", key = UserDaoImpl.PREFIX + "(#user.getId())")
    public Boolean deleteUserById(int id) {

        if (userDao.deleteUserById(id)) {
            redisUtils.sendMessage("aesop", new Date() + ": deleteUserById:" + id);
            return true;
        }

        return false;
    }

    @Override
    public Boolean updateUser(User user) {

        if (userDao.updateUser(user)) {
            redisUtils.sendMessage("aesop", new Date() + ": updateUser:" + user.getId());
            return true;
        }

        return false;

    }

    @Override
//    @Cacheable(value = "content", key = UserDaoImpl.PREFIX + "(#user.getId())")
    public User findUserById(int id) throws IOException {

        redisUtils.sendMessage("aesop", new Date() + ": findUserById:" + id);
        return userDao.findUserById(id);
    }

    @Override
    public List<User> findAll() throws IOException {

        redisUtils.sendMessage("aesop", new Date() + ": findAll");
        return userDao.findAll();
    }

    @Override
    public Boolean flushDb() {

        if (userDao.flushDb()) {
            redisUtils.sendMessage("aesop", new Date() + ": Flush DB");
            return true;
        }

        return false;
    }
}
