package dao;

import entity.User;
import java.util.*;
import java.util.stream.Collectors;

public class UserDao {
    private static final Map<Long, User> users = new HashMap<>();
    private static Long nextId = 1L;

    static {
        // 初始化测试数据
        User admin = new User("admin", "admin123", "ADMIN", null);
        admin.setNickname("系统管理员");
        admin.setEmail("admin@company.com");
        admin.setDepartment("信息技术部");
        admin.setPermissions(Arrays.asList("CREATE_USER", "UPDATE_USER", "DELETE_USER", "CREATE_PARAMETER", "UPDATE_PARAMETER", "DELETE_PARAMETER", "VIEW_PARAMETER"));
        createUser(admin);

        User manager = new User("manager1", "manager123", "MANAGER", 1L);
        manager.setNickname("部门经理");
        manager.setEmail("manager1@company.com");
        manager.setDepartment("业务部");
        manager.setPermissions(Arrays.asList("CREATE_PARAMETER", "UPDATE_PARAMETER", "VIEW_PARAMETER"));
        createUser(manager);

        User employee = new User("employee1", "emp123", "EMPLOYEE", 2L);
        employee.setNickname("普通员工");
        employee.setEmail("employee1@company.com");
        employee.setDepartment("业务部");
        employee.setPermissions(Arrays.asList("VIEW_PARAMETER"));
        createUser(employee);
    }

    public static synchronized User createUser(User user) {
        user.setId(nextId++);
        users.put(user.getId(), user);
        return user;
    }

    public static User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        }
        return null;
    }

    public static boolean deleteUser(Long id) {
        return users.remove(id) != null;
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

    public static List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public static List<User> findByRole(String role) {
        return users.values().stream()
                .filter(user -> user.getRole().equals(role))
                .collect(Collectors.toList());
    }

    public static List<User> findSubordinates(Long parentId) {
        return users.values().stream()
                .filter(user -> Objects.equals(user.getParentId(), parentId))
                .collect(Collectors.toList());
    }

    public static List<User> findWithPagination(int page, int pageSize) {
        List<User> allUsers = findAll();
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, allUsers.size());

        if (start >= allUsers.size()) {
            return new ArrayList<>();
        }

        return allUsers.subList(start, end);
    }

    public static long count() {
        return users.size();
    }
}
