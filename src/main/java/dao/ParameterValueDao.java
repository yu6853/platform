package dao;

import entity.ParameterValue;
import java.util.*;
import java.util.stream.Collectors;

public class ParameterValueDao {
    private static final Map<Long, ParameterValue> parameterValues = new HashMap<>();
    private static Long nextId = 1L;

    public static synchronized ParameterValue createParameterValue(ParameterValue parameterValue) {
        parameterValue.setId(nextId++);
        parameterValues.put(parameterValue.getId(), parameterValue);
        return parameterValue;
    }

    public static ParameterValue findById(Long id) {
        return parameterValues.get(id);
    }

    public static List<ParameterValue> findAll() {
        return new ArrayList<>(parameterValues.values());
    }

    public static List<ParameterValue> findByParameterId(Long parameterId) {
        return parameterValues.values().stream()
                .filter(pv -> Objects.equals(pv.getParameterId(), parameterId))
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt())) // 按时间倒序
                .collect(Collectors.toList());
    }

    public static List<ParameterValue> findByCreator(Long createdBy) {
        return parameterValues.values().stream()
                .filter(pv -> Objects.equals(pv.getCreatedBy(), createdBy))
                .collect(Collectors.toList());
    }

    public static List<ParameterValue> findWithPagination(int page, int pageSize, Long parameterId) {
        List<ParameterValue> filtered = parameterId != null ?
                findByParameterId(parameterId) : findAll();

        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, filtered.size());

        if (start >= filtered.size()) {
            return new ArrayList<>();
        }

        return filtered.subList(start, end);
    }

    public static long count(Long parameterId) {
        if (parameterId != null) {
            return findByParameterId(parameterId).size();
        }
        return parameterValues.size();
    }
}
