package com.hzero.demo.springwithredis.spring.dao.impl;

import com.hzero.demo.springwithredis.spring.dao.UserDao;
import com.hzero.demo.springwithredis.spring.model.User;
import com.hzero.demo.springwithredis.redis.util.RedisUtils;
import com.hzero.demo.springwithredis.util.EncryptUtil;
import com.hzero.demo.springwithredis.util.JacksonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

@Repository(value = "userDaoImpl")
public class UserDaoImpl implements UserDao {

    public static final String KEY = "AESOP";

    public static final String PREFIX = "USER_";

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    JacksonUtil jacksonUtil;

    @Autowired
    EncryptUtil encryptUtil;

    @Override
    public Boolean saveUser(User user) throws Exception {

        if (redisUtils.hasKey(UserDaoImpl.PREFIX + user.getId())) {
            System.out.println("用户ID:" + user.getId() + "已存在，请检查！");
            return false;
        }

        try {
            String jsonString = jacksonUtil.obj2json(user);

            jsonString = encryptUtil.AESencode(jsonString, UserDaoImpl.KEY);
            redisUtils.set(UserDaoImpl.PREFIX + user.getId(), jsonString, 0);

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }

    }

    @Override
    public Boolean saveUsers(List<User> users) throws Exception {

        Map<String, String> map = new HashMap<String, String>();

        //list to map
        for (User user : users) {

            if (redisUtils.hasKey(UserDaoImpl.PREFIX + user.getId())) {
                System.out.println("用户ID:" + user.getId() + "已存在，请检查！");
                return false;
            }

            String jsonString = jacksonUtil.obj2json(user);

            jsonString = encryptUtil.AESencode(jsonString, UserDaoImpl.KEY);
            map.put(UserDaoImpl.PREFIX + user.getId(), jsonString);

        }

        try {
            redisUtils.getRedisTemplate().executePipelined(new RedisCallback<String>() {
                @Override
                public String doInRedis(RedisConnection connection) throws DataAccessException {
                    map.forEach((key, value)
                            -> connection.set(redisUtils.getRedisTemplate().getKeySerializer().serialize(key),
                            redisUtils.getRedisTemplate().getValueSerializer().serialize(value)));
                    return null;
                }
            });

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Boolean deleteUserById(int id) {

        if (!redisUtils.hasKey(UserDaoImpl.PREFIX + id)) {
            System.out.println("用户ID:" + id + "不存在，请检查！");
            return false;
        }

        try {

            redisUtils.delete(UserDaoImpl.PREFIX + id);

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }


    }

    @Override
    public Boolean updateUser(User user) {

        if (!redisUtils.hasKey(UserDaoImpl.PREFIX + user.getId())) {
            System.out.println("用户ID:" + user.getId() + "不存在，请检查！");
            return false;
        }

        try {
            String jsonString = jacksonUtil.obj2json(user);

            jsonString = encryptUtil.AESencode(jsonString, UserDaoImpl.KEY);

            redisUtils.set(UserDaoImpl.PREFIX + user.getId(), jsonString, 0);

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }

    }

    @Override
    public User findUserById(int id) throws IOException {

        if (!redisUtils.hasKey(UserDaoImpl.PREFIX + id)) {
            System.out.println("用户ID:" + id + "不存在，请检查！");
            return null;
        }

        //取出redis缓存中的数据
        String jsonString = (String) redisUtils.get(UserDaoImpl.PREFIX + id);

        //对json数据解密
        jsonString = encryptUtil.AESdecode(jsonString, UserDaoImpl.KEY);

        //JSON 2 POJO
        return jacksonUtil.json2pojo(jsonString, User.class);

    }

    @Override
    public List<User> findAll() throws IOException {

        List<User> users = new ArrayList<>();

        //模糊查询KEY
        Set<String> keys = redisUtils.getRedisTemplate().keys(UserDaoImpl.PREFIX + "*");
        //得到KEY对应的结果集
        List<String> resultStr = redisUtils.getRedisTemplate().opsForValue().multiGet(keys);

        //结果集批量转化为pojo
        for (String value : resultStr) {

            User user = jacksonUtil.json2pojo(encryptUtil.AESdecode(value, UserDaoImpl.KEY), User.class);
            users.add(user);

        }

        Collections.sort(users);
        return users;

    }

    @Override
    public Boolean flushDb() {

        try {
            Set<String> keys = redisUtils.getRedisTemplate().keys(UserDaoImpl.PREFIX + "*");
            redisUtils.getRedisTemplate().delete(keys);

            return true;
        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }

    }

}
