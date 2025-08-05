import controller.RestApiController;
import controller.SystemController;
import dto.ApiResponse;
import entity.User;
import service.AuthService;
import util.JsonUtil;

public class Application {

    private static RestApiController restApiController = new RestApiController();
    private static SystemController systemController = new SystemController();

    public static void main(String[] args) {
        System.out.println("=== 参数管控系统启动 ===");

        // 演示系统功能
        demonstrateSystem();
    }

    private static void demonstrateSystem() {
        System.out.println("\n1. 系统信息:");
        ApiResponse<?> systemInfo = systemController.getSystemInfo();
        System.out.println(JsonUtil.toJson(systemInfo));

        System.out.println("\n2. 健康检查:");
        ApiResponse<?> health = systemController.healthCheck();
        System.out.println(JsonUtil.toJson(health));

        System.out.println("\n3. 用户登录:");
        ApiResponse<?> loginResult = restApiController.login("admin", "admin123");
        System.out.println(JsonUtil.toJson(loginResult));

        System.out.println("\n4. 获取当前用户:");
        ApiResponse<User> currentUser = restApiController.getCurrentUser();
        System.out.println(JsonUtil.toJson(currentUser));

        System.out.println("\n5. 获取用户列表:");
        ApiResponse<?> users = restApiController.getUsers(1, 10, null, null, null, "parent,children");
        System.out.println(JsonUtil.toJson(users));

        System.out.println("\n6. 获取参数列表:");
        ApiResponse<?> parameters = restApiController.getParameters(1, 10, null, "sort", null, "creator,parameter_values");
        System.out.println(JsonUtil.toJson(parameters));

        System.out.println("\n7. 获取参数历史:");
        ApiResponse<?> parameterValues = restApiController.getParameterValues(1L, 1, 5);
        System.out.println(JsonUtil.toJson(parameterValues));

        System.out.println("\n8. 创建新参数:");
        String newParameterJson = """
            {
                "name": "测试参数",
                "key": "test_parameter",
                "description": "这是一个测试参数",
                "type": "STRING",
                "value": "test_value",
                "category": "测试分类",
                "group": "测试组",
                "scope": "GLOBAL"
            }
            """;
        ApiResponse<?> createResult = restApiController.createParameter(newParameterJson);
        System.out.println(JsonUtil.toJson(createResult));

        System.out.println("\n9. 用户退出:");
        ApiResponse<Void> logoutResult = restApiController.logout();
        System.out.println(JsonUtil.toJson(logoutResult));

        System.out.println("\n=== 系统演示完成 ===");
    }
}
