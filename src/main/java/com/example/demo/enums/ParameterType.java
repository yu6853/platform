package com.example.demo.enums;

public enum ParameterType {
    BOOLEAN("勾选"),
    INTEGER("整数值"),
    FLOAT("精确值"),
    ENUM("下拉菜单");
    private String description;
    ParameterType(String description){
        this.description = description;
    }
    public String getDescription(){
        return description;
    }
}
