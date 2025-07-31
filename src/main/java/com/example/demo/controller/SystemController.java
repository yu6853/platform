package com.example.demo.controller;

import com.example.demo.service.AuthService;
import com.example.demo.service.ParameterService;
import com.example.demo.entity.Parameter;
import com.example.demo.enums.ParameterType;
import java.util.List;
import java.util.Scanner;
// SystemController.java - 系统控制器
public class SystemController {
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("=== 集团平台参数管控系统 ===");

        while (true) {
            if (!AuthService.isLoggedIn()) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private void showLoginMenu() {
        System.out.println("\n请登录:");
        System.out.print("用户名: ");
        String username = scanner.nextLine();
        System.out.print("密码: ");
        String password = scanner.nextLine();

        if (AuthService.login(username, password)) {
            System.out.println("登录成功! 欢迎 " + AuthService.getCurrentUser().getUsername() +
                    " (" + AuthService.getCurrentUser().getRole().getDescription() + ")");
        } else {
            System.out.println("登录失败，请检查用户名和密码！");
        }
    }

    private void showMainMenu() {
        System.out.println("\n=== 主菜单 ===");
        System.out.println("1. 查看参数列表");
        System.out.println("2. 创建参数");
        System.out.println("3. 更新参数");
        System.out.println("4. 删除参数");
        System.out.println("5. 退出登录");
        System.out.print("请选择操作: ");

        String choice = scanner.nextLine();

        try {
            switch (choice) {
                case "1":
                    listParameters();
                    break;
                case "2":
                    createParameter();
                    break;
                case "3":
                    updateParameter();
                    break;
                case "4":
                    deleteParameter();
                    break;
                case "5":
                    AuthService.logout();
                    System.out.println("已退出登录");
                    break;
                default:
                    System.out.println("无效选择");
            }
        } catch (SecurityException e) {
            System.out.println("权限错误: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("操作失败: " + e.getMessage());
        }
    }

    private void listParameters() {
        List<Parameter> parameters = ParameterService.getAllParameters();
        System.out.println("\n=== 参数列表 ===");
        if (parameters.isEmpty()) {
            System.out.println("暂无参数");
            return;
        }

        for (Parameter param : parameters) {
            System.out.printf("ID: %d, 名称: %s, 类型: %s, 值: %s, 描述: %s%n",
                    param.getId(), param.getName(), param.getType().getDescription(),
                    param.getValue(), param.getDescription());
        }
    }

    private void createParameter() {
        System.out.println("\n=== 创建参数 ===");
        System.out.print("参数名称: ");
        String name = scanner.nextLine();

        System.out.print("参数描述: ");
        String description = scanner.nextLine();

        System.out.println("参数类型:");
        System.out.println("1. 勾选(布尔值)");
        System.out.println("2. 整数值");
        System.out.println("3. 精确值(浮点数)");
        System.out.println("4. 下拉菜单(枚举)");
        System.out.print("选择类型: ");

        String typeChoice = scanner.nextLine();
        ParameterType type;

        switch (typeChoice) {
            case "1": type = ParameterType.BOOLEAN; break;
            case "2": type = ParameterType.INTEGER; break;
            case "3": type = ParameterType.FLOAT; break;
            case "4": type = ParameterType.ENUM; break;
            default:
                System.out.println("无效的类型选择");
                return;
        }

        System.out.print("参数值: ");
        String value = scanner.nextLine();

        String options = null;
        if (type == ParameterType.ENUM) {
            System.out.print("枚举选项(用逗号分隔): ");
            options = scanner.nextLine();
        }

        Parameter parameter = ParameterService.createParameter(null, name, description, type, value, options, AuthService.getCurrentUser().getId());
        System.out.println("参数创建成功，ID: " + parameter.getId());
    }

    private void updateParameter() {
        System.out.println("\n=== 更新参数 ===");
        listParameters();

        System.out.print("请输入要更新的参数ID: ");
        String idStr = scanner.nextLine();

        try {
            Long id = Long.parseLong(idStr);
            System.out.print("新的参数值: ");
            String newValue = scanner.nextLine();

            Parameter parameter = ParameterService.updateParameter(id, newValue);
            System.out.println("参数更新成功");
        } catch (NumberFormatException e) {
            System.out.println("无效的ID格式");
        }
    }

    private void deleteParameter() {
        System.out.println("\n=== 删除参数 ===");
        listParameters();

        System.out.print("请输入要删除的参数ID: ");
        String idStr = scanner.nextLine();

        try {
            Long id = Long.parseLong(idStr);
            ParameterService.deleteParameter(id);
            System.out.println("参数删除成功");
        } catch (NumberFormatException e) {
            System.out.println("无效的ID格式");
        }
    }
}
