package service;

import dao.ParameterDao;
import dao.ParameterValueDao;
import dao.UserDao;
import dto.PageResult;
import entity.Parameter;
import entity.ParameterValue;
import util.ValidationUtil;
import java.util.*;
import java.util.stream.Collectors;

public class ParameterService {

    public PageResult<Parameter> getParameters(Map<String, Object> params) {
        if (!AuthService.hasPermission("VIEW_PARAMETER")) {
            throw new SecurityException("没有查看参数的权限");
        }

        int page = (Integer) params.getOrDefault("page", 1);
        int pageSize = (Integer) params.getOrDefault("pageSize", 20);
        String filter = (String) params.get("filter");
        String sort = (String) params.get("sort");
        String fields = (String) params.get("fields");
        String appends = (String) params.get("appends");

        List<Parameter> parameters = ParameterDao.findAll();

        // 应用过滤器
        if (filter != null && !filter.isEmpty()) {
            parameters = applyFilter(parameters, filter);
        }

        // 应用排序
        if (sort != null && !sort.isEmpty()) {
            parameters = applySort(parameters, sort);
        }

        long total = parameters.size();

        // 分页
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, parameters.size());
        if (start >= parameters.size()) {
            parameters = new ArrayList<>();
        } else {
            parameters = parameters.subList(start, end);
        }

        // 应用关联查询
        if (appends != null && !appends.isEmpty()) {
            parameters = applyAppends(parameters, appends);
        }

        return new PageResult<>(parameters, total, page, pageSize);
    }

    public Parameter getParameterById(Long id, Map<String, Object> params) {
        if (!AuthService.canAccessParameter(id)) {
            throw new SecurityException("没有访问该参数的权限");
        }

        Parameter parameter = ParameterDao.findById(id);
        if (parameter == null) {
            return null;
        }

        String appends = (String) params.get("appends");

        // 应用关联查询
        if (appends != null && !appends.isEmpty()) {
            parameter = applyAppends(Arrays.asList(parameter), appends).get(0);
        }

        return parameter;
    }

    public Parameter createParameter(Parameter parameter) {
        if (!AuthService.hasPermission("CREATE_PARAMETER")) {
            throw new SecurityException("没有创建参数的权限");
        }

        // 验证参数键唯一性
        if (ParameterDao.findByKey(parameter.getKey()) != null) {
            throw new IllegalArgumentException("参数键已存在");
        }

        // 验证参数值
        ValidationUtil.validateParameterValue(parameter.getType(), parameter.getValue(), parameter.getValidationRules());

        parameter.setCreatedBy(AuthService.getCurrentUser().getId());
        parameter.setUpdatedBy(AuthService.getCurrentUser().getId());

        Parameter createdParameter = ParameterDao.createParameter(parameter);

        // 记录参数值历史
        ParameterValue parameterValue = new ParameterValue(
                createdParameter.getId(),
                createdParameter.getValue(),
                null,
                "参数创建",
                AuthService.getCurrentUser().getId()
        );
        ParameterValueDao.createParameterValue(parameterValue);

        return createdParameter;
    }

    public Parameter updateParameter(Parameter parameter) {
        if (!AuthService.hasPermission("UPDATE_PARAMETER")) {
            throw new SecurityException("没有更新参数的权限");
        }

        Parameter existingParameter = ParameterDao.findById(parameter.getId());
        if (existingParameter == null) {
            throw new IllegalArgumentException("参数不存在");
        }

        // 检查参数键唯一性（排除当前参数）
        Parameter paramWithSameKey = ParameterDao.findByKey(parameter.getKey());
        if (paramWithSameKey != null && !paramWithSameKey.getId().equals(parameter.getId())) {
            throw new IllegalArgumentException("参数键已存在");
        }

        // 验证参数值
        ValidationUtil.validateParameterValue(parameter.getType(), parameter.getValue(), parameter.getValidationRules());

        String previousValue = existingParameter.getValue();
        parameter.setUpdatedBy(AuthService.getCurrentUser().getId());
        parameter.setCreatedAt(existingParameter.getCreatedAt()); // 保持创建时间不变
        parameter.setCreatedBy(existingParameter.getCreatedBy()); // 保持创建者不变

        Parameter updatedParameter = ParameterDao.updateParameter(parameter);

        // 如果值发生变化，记录参数值历史
        if (!Objects.equals(previousValue, parameter.getValue())) {
            ParameterValue parameterValue = new ParameterValue(
                    updatedParameter.getId(),
                    updatedParameter.getValue(),
                    previousValue,
                    "参数更新",
                    AuthService.getCurrentUser().getId()
            );
            ParameterValueDao.createParameterValue(parameterValue);
        }

        return updatedParameter;
    }

    public void deleteParameter(Long id) {
        if (!AuthService.hasPermission("DELETE_PARAMETER")) {
            throw new SecurityException("没有删除参数的权限");
        }

        Parameter parameter = ParameterDao.findById(id);
        if (parameter == null) {
            throw new IllegalArgumentException("参数不存在");
        }

        ParameterDao.deleteParameter(id);
    }

    public PageResult<ParameterValue> getParameterValues(Map<String, Object> params) {
        if (!AuthService.hasPermission("VIEW_PARAMETER")) {
            throw new SecurityException("没有查看参数历史的权限");
        }

        Long parameterId = (Long) params.get("parameterId");
        int page = (Integer) params.getOrDefault("page", 1);
        int pageSize = (Integer) params.getOrDefault("pageSize", 20);

        List<ParameterValue> values = ParameterValueDao.findWithPagination(page, pageSize, parameterId);
        long total = ParameterValueDao.count(parameterId);

        // 填充关联数据
        for (ParameterValue value : values) {
            if (value.getParameterId() != null) {
                value.setParameter(ParameterDao.findById(value.getParameterId()));
            }
            if (value.getCreatedBy() != null) {
                value.setCreator(UserDao.findById(value.getCreatedBy()));
            }
        }

        return new PageResult<>(values, total, page, pageSize);
    }

    private List<Parameter> applyFilter(List<Parameter> parameters, String filter) {
        String lowerFilter = filter.toLowerCase();
        return parameters.stream()
                .filter(param ->
                        (param.getName() != null && param.getName().toLowerCase().contains(lowerFilter)) ||
                                (param.getKey() != null && param.getKey().toLowerCase().contains(lowerFilter)) ||
                                (param.getDescription() != null && param.getDescription().toLowerCase().contains(lowerFilter))
                )
                .collect(Collectors.toList());
    }

    private List<Parameter> applySort(List<Parameter> parameters, String sort) {
        if ("created_at".equals(sort)) {
            return parameters.stream()
                    .sorted(Comparator.comparing(Parameter::getCreatedAt))
                    .collect(Collectors.toList());
        } else if ("-created_at".equals(sort)) {
            return parameters.stream()
                    .sorted(Comparator.comparing(Parameter::getCreatedAt).reversed())
                    .collect(Collectors.toList());
        } else if ("sort".equals(sort)) {
            return parameters.stream()
                    .sorted(Comparator.comparing(Parameter::getSort, Comparator.nullsLast(Comparator.naturalOrder())))
                    .collect(Collectors.toList());
        }
        return parameters;
    }

    private List<Parameter> applyAppends(List<Parameter> parameters, String appends) {
        Set<String> appendFields = new HashSet<>(Arrays.asList(appends.split(",")));

        for (Parameter parameter : parameters) {
            if (appendFields.contains("creator") && parameter.getCreatedBy() != null) {
                parameter.setCreator(UserDao.findById(parameter.getCreatedBy()));
            }
            if (appendFields.contains("updater") && parameter.getUpdatedBy() != null) {
                parameter.setUpdater(UserDao.findById(parameter.getUpdatedBy()));
            }
            if (appendFields.contains("parameter_values")) {
                parameter.setParameterValues(ParameterValueDao.findByParameterId(parameter.getId()));
            }
        }

        return parameters;
    }
}
