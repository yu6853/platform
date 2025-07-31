package com.example.demo.entity;

import com.example.demo.enums.UserRole;

public class User {
    private Long id;
    private String username;
    private String password;
    private UserRole role;
    private Long parentId;

    public User() {};

    public User(String username, String password, UserRole role, Long parentId){
        this.username = username;
        this.password = password;
        this.role = role;
        this.parentId = parentId;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public UserRole getRole(){
        return role;
    }

    public void setRole(UserRole role){
        this.role = role;
    }

    public Long getParentId(){
        return parentId;
    }

    public void setParentId(Long parentId){
        this.parentId = parentId;
    }
}
