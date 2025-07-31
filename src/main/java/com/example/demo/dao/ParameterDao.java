// ParameterDao.java - 参数数据访问
package com.example.demo.dao;


import com.example.demo.entity.Parameter;
import java.util.*;

public class ParameterDao {
    private static Map<Long, Parameter> parameters = new HashMap<>();
    private static Long nextId = 1L;

    public static Parameter createParameter(Parameter parameter) {
        parameter.setId(nextId++);
        parameters.put(parameter.getId(), parameter);
        return parameter;
    }

    public static Parameter updateParameter(Parameter parameter) {
        parameters.put(parameter.getId(), parameter);
        return parameter;
    }

    public static void deleteParameter(Long id) {
        parameters.remove(id);
    }

    public static Parameter findById(Long id) {
        return parameters.get(id);
    }

    public static List<Parameter> findAll() {
        return new ArrayList<>(parameters.values());
    }

    public static List<Parameter> findByCreator(Long createdBy) {
        List<Parameter> result = new ArrayList<>();
        for (Parameter param : parameters.values()) {
            if (Objects.equals(param.getCreatedBy(), createdBy)) {
                result.add(param);
            }
        }
        return result;
    }
}
