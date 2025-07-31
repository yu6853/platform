package com.example.demo.service;

import com.example.demo.dao.ParameterDao;
import com.example.demo.entity.Parameter;
import com.example.demo.enums.ParameterType;
import java.util.*;

public class ParameterService {

    public static Parameter createParameter(Long id, String name, String description, ParameterType type, String value, String options,Long cretedBy) {
        if (!AuthService.hasPermission("CREATE_PARAMETER")) {
            throw new SecurityException("没有创建参数的权限");
        }

        // 验证参数值
        validateParameterValue(type, value);

        Parameter parameter = new Parameter(id, name, description, type, value, options, AuthService.getCurrentUser().getId());
        return ParameterDao.createParameter(parameter);
    }

    public static Parameter updateParameter(Long id, String value) {
        if (!AuthService.hasPermission("UPDATE_PARAMETER")) {
            throw new SecurityException("没有更新参数的权限");
        }

        Parameter parameter = ParameterDao.findById(id);
        if (parameter == null) {
            throw new IllegalArgumentException("参数不存在");
        }

        // 验证参数值
        validateParameterValue(parameter.getType(), value);

        parameter.setValue(value);
        return ParameterDao.updateParameter(parameter);
    }

    public static void deleteParameter(Long id) {
        if (!AuthService.hasPermission("DELETE_PARAMETER")) {
            throw new SecurityException("没有删除参数的权限");
        }

        ParameterDao.deleteParameter(id);
    }

    public static List<Parameter> getAllParameters() {
        if (!AuthService.hasPermission("VIEW_PARAMETER")) {
            throw new SecurityException("没有查看参数的权限");
        }

        return ParameterDao.findAll();
    }

    private static void validateParameterValue(ParameterType type, String value) {
        switch (type) {
            case BOOLEAN:
                if (!"true".equals(value) && !"false".equals(value)) {
                    throw new IllegalArgumentException("布尔值只能是true或false");
                }
                break;
            case INTEGER:
                try {
                    Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("整数值格式不正确");
                }
                break;
            case FLOAT:
                try {
                    Float.parseFloat(value);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("浮点数格式不正确");
                }
                break;
            case ENUM:
                // 枚举值验证可以在这里添加
                break;
        }
    }
}