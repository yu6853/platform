package service;

import dao.UserDao;
import entity.User;
import java.util.List;

public class AuthService {
    private static User currentUser = null;

    public static boolean login(String username, String password) {
        User user = UserDao.findByUsername(username);
        if (user != null && user.getPassword().equals(password) && "ACTIVE".equals(user.getStatus())) {
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

    public static boolean hasPermission(String permission) {
        if (currentUser == null) return false;

        List<String> permissions = currentUser.getPermissions();
        if (permissions == null) return false;

        return permissions.contains(permission) || "ADMIN".equals(currentUser.getRole());
    }

    public static boolean canAccessUser(Long userId) {
        if (currentUser == null) return false;

        // 管理员可以访问所有用户
        if ("ADMIN".equals(currentUser.getRole())) {
            return true;
        }

        // 用户只能访问自己
        if (currentUser.getId().equals(userId)) {
            return true;
        }

        // 经理可以访问下属
        if ("MANAGER".equals(currentUser.getRole())) {
            User targetUser = UserDao.findById(userId);
            return targetUser != null && currentUser.getId().equals(targetUser.getParentId());
        }

        return false;
    }

    public static boolean canAccessParameter(Long parameterId) {
        if (currentUser == null) return false;

        // 管理员可以访问所有参数
        if ("ADMIN".equals(currentUser.getRole())) {
            return true;
        }

        // 其他用户根据权限判断
        return hasPermission("VIEW_PARAMETER");
    }
}
