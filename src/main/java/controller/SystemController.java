package controller;

import dto.ApiResponse;
import service.AuthService;

import java.util.HashMap;
import java.util.Map;

// @RestController
// @RequestMapping("/api/system")
public class SystemController {

    // @GetMapping("/info")
    public ApiResponse<Map<String, Object>> getSystemInfo() {
        Map<String, Object> systemInfo = new HashMap<>();
        systemInfo.put("name", "参数管控系统");
        systemInfo.put("version", "1.0.0");
        systemInfo.put("description", "集团平台参数管控系统");
        systemInfo.put("author", "开发团队");

        return ApiResponse.success(systemInfo);
    }

    // @GetMapping("/health")
    public ApiResponse<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", System.currentTimeMillis());

        // 检查各个组件状态
        Map<String, String> components = new HashMap<>();
        components.put("database", "UP"); // 实际项目中检查数据库连接
        components.put("cache", "UP"); // 实际项目中检查缓存状态
        components.put("auth", AuthService.isLoggedIn() ? "UP" : "DOWN");

        health.put("components", components);

        return ApiResponse.success(health);
    }

    // @GetMapping("/stats")
    public ApiResponse<Map<String, Object>> getSystemStats() {
        if (!AuthService.hasPermission("VIEW_SYSTEM_STATS")) {
            return ApiResponse.error("没有查看系统统计的权限", 403);
        }

        Map<String, Object> stats = new HashMap<>();

        // 这里可以添加各种统计信息
        stats.put("totalUsers", 0); // 从数据库获取
        stats.put("totalParameters", 0); // 从数据库获取
        stats.put("totalParameterValues", 0); // 从数据库获取
        stats.put("activeUsers", 0); // 从缓存或数据库获取

        return ApiResponse.success(stats);
    }
}
