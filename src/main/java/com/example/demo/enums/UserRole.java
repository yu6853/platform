package com.example.demo.enums;

public enum UserRole {
    ADMIN("管理员"),
    MANAGER("经理"),
    EMPLOYEE("员工");

    private String description;

    UserRole(String description){
        this.description = description;
    }
    public String getDescription(){
        return description;
    }
}
