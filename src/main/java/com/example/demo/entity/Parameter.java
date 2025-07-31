package com.example.demo.entity;

import com.example.demo.enums.ParameterType;

public class Parameter {
    private Long id;
    private String name;
    private String description;
    private ParameterType type;
    private String value;
    private String options;
    private Long createdBy;


    public Parameter(Long id, String name, String description, ParameterType type, String value, String options,
                     Long createdBy) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type; // Changed from Parameter to ParameterType
        this.value = value;
        this.options = options;
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public ParameterType getType() {
        return type;
    }
    public void setType(ParameterType type) {
        this.type = type;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getOptions() {
        return options;
    }
    public void setOptions(String options) {
        this.options = options;
    }
    public Long getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }


}