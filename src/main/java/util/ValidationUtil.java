package util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ValidationUtil {

    public static void validateParameterValue(String type, String value, String validationRules) {
        if (value == null || value.trim().isEmpty()) {
            return; // 空值验证由isRequired字段控制
        }

        switch (type) {
            case "BOOLEAN":
                validateBoolean(value);
                break;
            case "INTEGER":
                validateInteger(value, validationRules);
                break;
            case "FLOAT":
                validateFloat(value, validationRules);
                break;
            case "STRING":
                validateString(value, validationRules);
                break;
            case "ENUM":
                // 枚举验证通常在前端处理，这里可以添加额外验证
                break;
            default:
                // 未知类型，不进行验证
                break;
        }
    }

    private static void validateBoolean(String value) {
        if (!"true".equals(value) && !"false".equals(value)) {
            throw new IllegalArgumentException("布尔值只能是true或false");
        }
    }

    private static void validateInteger(String value, String validationRules) {
        try {
            int intValue = Integer.parseInt(value);

            if (validationRules != null && !validationRules.isEmpty()) {
                Map<String, Object> rules = JsonUtil.toMap(validationRules);

                if (rules.containsKey("min")) {
                    int min = ((Number) rules.get("min")).intValue();
                    if (intValue < min) {
                        throw new IllegalArgumentException("值不能小于" + min);
                    }
                }

                if (rules.containsKey("max")) {
                    int max = ((Number) rules.get("max")).intValue();
                    if (intValue > max) {
                        throw new IllegalArgumentException("值不能大于" + max);
                    }
                }
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("整数值格式不正确");
        }
    }

    private static void validateFloat(String value, String validationRules) {
        try {
            float floatValue = Float.parseFloat(value);

            if (validationRules != null && !validationRules.isEmpty()) {
                Map<String, Object> rules = JsonUtil.toMap(validationRules);

                if (rules.containsKey("min")) {
                    float min = ((Number) rules.get("min")).floatValue();
                    if (floatValue < min) {
                        throw new IllegalArgumentException("值不能小于" + min);
                    }
                }

                if (rules.containsKey("max")) {
                    float max = ((Number) rules.get("max")).floatValue();
                    if (floatValue > max) {
                        throw new IllegalArgumentException("值不能大于" + max);
                    }
                }
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("浮点数格式不正确");
        }
    }

    private static void validateString(String value, String validationRules) {
        if (validationRules != null && !validationRules.isEmpty()) {
            Map<String, Object> rules = JsonUtil.toMap(validationRules);

            if (rules.containsKey("minLength")) {
                int minLength = ((Number) rules.get("minLength")).intValue();
                if (value.length() < minLength) {
                    throw new IllegalArgumentException("字符串长度不能少于" + minLength + "个字符");
                }
            }

            if (rules.containsKey("maxLength")) {
                int maxLength = ((Number) rules.get("maxLength")).intValue();
                if (value.length() > maxLength) {
                    throw new IllegalArgumentException("字符串长度不能超过" + maxLength + "个字符");
                }
            }

            if (rules.containsKey("pattern")) {
                String pattern = (String) rules.get("pattern");
                if (!value.matches(pattern)) {
                    throw new IllegalArgumentException("字符串格式不符合要求");
                }
            }
        }
    }
}
