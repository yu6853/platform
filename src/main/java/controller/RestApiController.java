package controller;

import dto.ApiResponse;
import dto.PageResult;
import entity.Parameter;
import entity.ParameterValue;
import entity.User;
import service.AuthService;
import service.ParameterService;
import service.UserService;
import util.JsonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 注意：这里使用伪注解，实际项目中需要使用真实的Spring注解
// @RestController
// @RequestMapping("/api")
// @CrossOrigin(origins = "*")
public class RestApiController {

    private UserService userService = new UserService();
    private ParameterService parameterService = new ParameterService();

    // ========== 认证API ==========

    // @PostMapping("/auth/login")
    public ApiResponse<Map<String, Object>> login(String username, String password) {
        try {
            if (AuthService.login(username, password)) {
                User currentUser = AuthService.getCurrentUser();
                Map<String, Object> result = new HashMap<>();
                result.put("user", currentUser);
                result.put("token", "mock-jwt-token"); // 实际项目中应生成真实JWT
                return ApiResponse.success(result);
            } else {
                return ApiResponse.error("用户名或密码错误", 401);
            }
        } catch (Exception e) {
            return ApiResponse.error("登录失败: " + e.getMessage(), 500);
        }
    }

    // @PostMapping("/auth/logout")
    public ApiResponse<Void> logout() {
        AuthService.logout();
        return ApiResponse.success(null);
    }

    // @GetMapping("/auth/me")
    public ApiResponse<User> getCurrentUser() {
        User currentUser = AuthService.getCurrentUser();
        if (currentUser == null) {
            return ApiResponse.error("未登录", 401);
        }
        return ApiResponse.success(currentUser);
    }

    // ========== 用户管理API ==========

    // @GetMapping("/users")
    public ApiResponse<List<User>> getUsers(
            int page, int pageSize, String filter, String sort, String fields, String appends) {

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("page", page > 0 ? page : 1);
            params.put("pageSize", pageSize > 0 ? pageSize : 20);
            params.put("filter", filter);
            params.put("sort", sort);
            params.put("fields", fields);
            params.put("appends", appends);

            PageResult<User> result = userService.getUsers(params);

            return ApiResponse.success(result.getData(), result.getMeta());
        } catch (SecurityException e) {
            return ApiResponse.error(e.getMessage(), 403);
        } catch (Exception e) {
            return ApiResponse.error("获取用户列表失败: " + e.getMessage(), 500);
        }
    }

    // @GetMapping("/users/{id}")
    public ApiResponse<User> getUser(Long id, String fields, String appends) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("fields", fields);
            params.put("appends", appends);

            User user = userService.getUserById(id, params);
            if (user == null) {
                return ApiResponse.error("用户不存在", 404);
            }
            return ApiResponse.success(user);
        } catch (SecurityException e) {
            return ApiResponse.error(e.getMessage(), 403);
        } catch (Exception e) {
            return ApiResponse.error("获取用户信息失败: " + e.getMessage(), 500);
        }
    }

    // @PostMapping("/users")
    public ApiResponse<User> createUser(String userJson) {
        try {
            User user = JsonUtil.fromJson(userJson, User.class);
            User createdUser = userService.createUser(user);
            return ApiResponse.success(createdUser);
        } catch (SecurityException e) {
            return ApiResponse.error(e.getMessage(), 403);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(e.getMessage(), 400);
        } catch (Exception e) {
            return ApiResponse.error("创建用户失败: " + e.getMessage(), 500);
        }
    }

    // @PutMapping("/users/{id}")
    public ApiResponse<User> updateUser(Long id, String userJson) {
        try {
            User user = JsonUtil.fromJson(userJson, User.class);
            user.setId(id);
            User updatedUser = userService.updateUser(user);
            return ApiResponse.success(updatedUser);
        } catch (SecurityException e) {
            return ApiResponse.error(e.getMessage(), 403);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(e.getMessage(), 400);
        } catch (Exception e) {
            return ApiResponse.error("更新用户失败: " + e.getMessage(), 500);
        }
    }

    // @DeleteMapping("/users/{id}")
    public ApiResponse<Void> deleteUser(Long id) {
        try {
            userService.deleteUser(id);
            return ApiResponse.success(null);
        } catch (SecurityException e) {
            return ApiResponse.error(e.getMessage(), 403);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(e.getMessage(), 400);
        } catch (Exception e) {
            return ApiResponse.error("删除用户失败: " + e.getMessage(), 500);
        }
    }

    // ========== 参数管理API ==========

    // @GetMapping("/parameters")
    public ApiResponse<List<Parameter>> getParameters(
            int page, int pageSize, String filter, String sort, String fields, String appends) {

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("page", page > 0 ? page : 1);
            params.put("pageSize", pageSize > 0 ? pageSize : 20);
            params.put("filter", filter);
            params.put("sort", sort);
            params.put("fields", fields);
            params.put("appends", appends);

            PageResult<Parameter> result = parameterService.getParameters(params);

            return ApiResponse.success(result.getData(), result.getMeta());
        } catch (SecurityException e) {
            return ApiResponse.error(e.getMessage(), 403);
        } catch (Exception e) {
            return ApiResponse.error("获取参数列表失败: " + e.getMessage(), 500);
        }
    }

    // @GetMapping("/parameters/{id}")
    public ApiResponse<Parameter> getParameter(Long id, String fields, String appends) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("fields", fields);
            params.put("appends", appends);

            Parameter parameter = parameterService.getParameterById(id, params);
            if (parameter == null) {
                return ApiResponse.error("参数不存在", 404);
            }
            return ApiResponse.success(parameter);
        } catch (SecurityException e) {
            return ApiResponse.error(e.getMessage(), 403);
        } catch (Exception e) {
            return ApiResponse.error("获取参数信息失败: " + e.getMessage(), 500);
        }
    }

    // @PostMapping("/parameters")
    public ApiResponse<Parameter> createParameter(String parameterJson) {
        try {
            Parameter parameter = JsonUtil.fromJson(parameterJson, Parameter.class);
            Parameter createdParameter = parameterService.createParameter(parameter);
            return ApiResponse.success(createdParameter);
        } catch (SecurityException e) {
            return ApiResponse.error(e.getMessage(), 403);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(e.getMessage(), 400);
        } catch (Exception e) {
            return ApiResponse.error("创建参数失败: " + e.getMessage(), 500);
        }
    }

    // @PutMapping("/parameters/{id}")
    public ApiResponse<Parameter> updateParameter(Long id, String parameterJson) {
        try {
            Parameter parameter = JsonUtil.fromJson(parameterJson, Parameter.class);
            parameter.setId(id);
            Parameter updatedParameter = parameterService.updateParameter(parameter);
            return ApiResponse.success(updatedParameter);
        } catch (SecurityException e) {
            return ApiResponse.error(e.getMessage(), 403);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(e.getMessage(), 400);
        } catch (Exception e) {
            return ApiResponse.error("更新参数失败: " + e.getMessage(), 500);
        }
    }

    // @DeleteMapping("/parameters/{id}")
    public ApiResponse<Void> deleteParameter(Long id) {
        try {
            parameterService.deleteParameter(id);
            return ApiResponse.success(null);
        } catch (SecurityException e) {
            return ApiResponse.error(e.getMessage(), 403);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(e.getMessage(), 400);
        } catch (Exception e) {
            return ApiResponse.error("删除参数失败: " + e.getMessage(), 500);
        }
    }

    // @GetMapping("/parameters/{id}/values")
    public ApiResponse<List<ParameterValue>> getParameterValues(
            Long id, int page, int pageSize) {

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("parameterId", id);
            params.put("page", page > 0 ? page : 1);
            params.put("pageSize", pageSize > 0 ? pageSize : 20);

            PageResult<ParameterValue> result = parameterService.getParameterValues(params);

            return ApiResponse.success(result.getData(), result.getMeta());
        } catch (SecurityException e) {
            return ApiResponse.error(e.getMessage(), 403);
        } catch (Exception e) {
            return ApiResponse.error("获取参数历史失败: " + e.getMessage(), 500);
        }
    }

    // @GetMapping("/parameter-values")
    public ApiResponse<List<ParameterValue>> getAllParameterValues(
            int page, int pageSize) {

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("page", page > 0 ? page : 1);
            params.put("pageSize", pageSize > 0 ? pageSize : 20);

            PageResult<ParameterValue> result = parameterService.getParameterValues(params);

            return ApiResponse.success(result.getData(), result.getMeta());
        } catch (SecurityException e) {
            return ApiResponse.error(e.getMessage(), 403);
        } catch (Exception e) {
            return ApiResponse.error("获取参数历史失败: " + e.getMessage(), 500);
        }
    }
}
