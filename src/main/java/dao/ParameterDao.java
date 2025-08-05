package dao;

import entity.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class ParameterDao {
    private static final Map<Long, Parameter> parameters = new HashMap<>();
    private static Long nextId = 1L;

    static {
        // 初始化测试数据
        Parameter param1 = new Parameter("系统维护模式", "system_maintenance_mode", "系统是否处于维护模式", "BOOLEAN", "false");
        param1.setCategory("系统设置");
        param1.setGroup("基础配置");
        param1.setScope("GLOBAL");
        param1.setCreatedBy(1L);
        createParameter(param1);

        Parameter param2 = new Parameter("最大登录尝试次数", "max_login_attempts", "用户最大登录尝试次数", "INTEGER", "5");
        param2.setCategory("安全设置");
        param2.setGroup("登录控制");
        param2.setScope("GLOBAL");
        param2.setValidationRules("{\"min\": 1, \"max\": 10}");
        param2.setCreatedBy(1L);
        createParameter(param2);

        Parameter param3 = new Parameter("系统主题", "system_theme", "系统界面主题", "ENUM", "light");
        param3.setCategory("界面设置");
        param3.setGroup("外观配置");
        param3.setScope("USER");
        param3.setOptions("[\"light\", \"dark\", \"auto\"]");
        param3.setCreatedBy(2L);
        createParameter(param3);
    }

    public static synchronized Parameter createParameter(Parameter parameter) {
        parameter.setId(nextId++);
        parameters.put(parameter.getId(), parameter);
        return parameter;
    }

    public static Parameter updateParameter(Parameter parameter) {
        if (parameters.containsKey(parameter.getId())) {
            parameters.put(parameter.getId(), parameter);
            return parameter;
        }
        return null;
    }

    public static boolean deleteParameter(Long id) {
        return parameters.remove(id) != null;
    }

    public static Parameter findById(Long id) {
        return parameters.get(id);
    }

    public static Parameter findByKey(String key) {
        return parameters.values().stream()
                .filter(param -> param.getKey().equals(key))
                .findFirst()
                .orElse(null);
    }

    public static List<Parameter> findAll() {
        return new ArrayList<>(parameters.values());
    }

    public static List<Parameter> findByCreator(Long createdBy) {
        return parameters.values().stream()
                .filter(param -> Objects.equals(param.getCreatedBy(), createdBy))
                .collect(Collectors.toList());
    }

    public static List<Parameter> findByCategory(String category) {
        return parameters.values().stream()
                .filter(param -> param.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    public static List<Parameter> findByScope(String scope) {
        return parameters.values().stream()
                .filter(param -> param.getScope().equals(scope))
                .collect(Collectors.toList());
    }

    public static List<Parameter> findWithPagination(int page, int pageSize) {
        List<Parameter> allParams = findAll();
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, allParams.size());

        if (start >= allParams.size()) {
            return new ArrayList<>();
        }

        return allParams.subList(start, end);
    }

    public static long count() {
        return parameters.size();
    }
}
