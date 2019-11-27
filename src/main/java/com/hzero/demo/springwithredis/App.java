package com.hzero.demo.springwithredis;

import com.hzero.demo.springwithredis.spring.model.User;
import com.hzero.demo.springwithredis.spring.service.impl.UserServiceImpl;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

@EnableCaching
public class App {

    public static void main(String[] args) throws Exception {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        UserServiceImpl userService = (UserServiceImpl) context.getBean("userServiceImpl");

        //测试前先清空缓存库
        userService.flushDb();

        //批量添加user
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            users.add(new User(i, i + "012345", "user_" + i));
        }
        userService.saveUsers(users);


        //添加单个user
        userService.saveUser(new User(11, 11 + "012345", "user_" + 11));

        for (User user : userService.findAll()) {
            System.out.println(user);
        }

        //删除USER 1 3 5
        userService.deleteUserById(1);
        userService.deleteUserById(3);
        userService.deleteUserById(5);

        //更新user 6
        userService.updateUser(new User(6, "666666", "user" + 6));

        System.out.println("查询剩下的user");
        List<User> userList = userService.findAll();
        for (User user : userList) {
            System.out.println(user);
        }

        System.out.println(userService.findUserById(11));


    }


}
