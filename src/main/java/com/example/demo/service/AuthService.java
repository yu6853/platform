package com.example.demo.service;

import com.example.demo.dao.UserDao;
import com.example.demo.entity.User;
public class AuthService {
    private static User currentUser = null;

    public static boolean login(String username, String password) {
        User user = UserDao.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public static void logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static boolean hasPermission(String action) {
        if (currentUser == null) return false;

        switch (currentUser.getRole()) {
            case ADMIN:
                return true; // 管理员有所有权限
            case MANAGER:
                return action.equals("CREATE_PARAMETER") || action.equals("UPDATE_PARAMETER") || action.equals("VIEW_PARAMETER");
            case EMPLOYEE:
                return action.equals("VIEW_PARAMETER");
            default:
                return false;
        }
    }
}