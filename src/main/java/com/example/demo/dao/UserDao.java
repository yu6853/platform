// UserDao.java - 用户数据访问
package com.example.demo.dao;

import com.example.demo.entity.User;
import com.example.demo.enums.UserRole;
import java.util.*;

public class UserDao {
    private static Map<Long, User> users = new HashMap<>();
    private static Long nextId = 1L;

    static {
        // 初始化一些测试数据
        createUser(new User("admin", "admin123", UserRole.ADMIN, null));
        createUser(new User("manager1", "manager123", UserRole.MANAGER, 1L));
        createUser(new User("employee1", "emp123", UserRole.EMPLOYEE, 2L));
    }

    public static User createUser(User user) {
        user.setId(nextId++);
        users.put(user.getId(), user);
        return user;
    }

    public static User findByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public static User findById(Long id) {
        return users.get(id);
    }

    public static List<User> findSubordinates(Long parentId) {
        List<User> subordinates = new ArrayList<>();
        for (User user : users.values()) {
            if (Objects.equals(user.getParentId(), parentId)) {
                subordinates.add(user);
            }
        }
        return subordinates;
    }
}
