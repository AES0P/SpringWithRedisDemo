package com.hzero.demo.springwithredis.redis.task;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * 可以利用 sprig 提供的task定时向数据库写入redis中缓存的数据
 */
@Component
public class ConnectionCheck {

    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;

    //    cron 是一种周期的表达式，六位从右至左分别对应的是年、月、日、时、分、秒，数字配合各种通配符可以表达种类丰富的定时执行周期。
    @Scheduled(cron = "0/10 * * * * *")// 每 10 S执行一次.在XML中配置后就不需要注解了，但是需要开启任务注解<task:annotation-driven />
    public void task() {
        try {
            checkConnection();
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    /**
     * 这个方法会受到redis最大连接数的限制，如果不处理好，一旦达到连接上限，会报异常
     *
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public void checkConnection() throws UnsupportedEncodingException, NoSuchAlgorithmException {

        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();

        System.out.println("Redis Connection is Closed: " + redisConnection.isClosed());

        redisConnection = null;
    }
}
