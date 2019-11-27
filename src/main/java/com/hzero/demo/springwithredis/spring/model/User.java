package com.hzero.demo.springwithredis.spring.model;


import java.io.Serializable;

public class User implements Serializable, Comparable<User> {

    private int id;

    private String passWord;

    private String name;

    public User() {
    }

    public User(int id, String passWord, String name) {
        this.id = id;
        this.passWord = passWord;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getPassWord() {
        return passWord;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "  id='" + id + '\'' +
                ", passWord='" + passWord + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int compareTo(User user) { //重写Comparable接口的compareTo方法
        return this.id - user.getId();// 根据id升序排列，降序修改相减顺序即可
    }
}
