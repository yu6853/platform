package service;

import dao.UserDao;
import dto.PageResult;
import entity.User;
import util.JsonUtil;
import java.util.*;
import java.util.stream.Collectors;

public class UserService {

    public PageResult<User> getUsers(Map<String, Object> params) {
        if (!AuthService.hasPermission("VIEW_USER")) {
            throw new SecurityException("没有查看用户的权限");
        }

        int page = (Integer) params.getOrDefault("page", 1);
        int pageSize = (Integer) params.getOrDefault("pageSize", 20);
        String filter = (String) params.get("filter");
        String sort = (String) params.get("sort");
        String fields = (String) params.get("fields");
        String appends = (String) params.get("appends");

        List<User> users = UserDao.findAll();

        // 应用过滤器
        if (filter != null && !filter.isEmpty()) {
            users = applyFilter(users, filter);
        }

        // 应用排序
        if (sort != null && !sort.isEmpty()) {
            users = applySort(users, sort);
        }

        long total = users.size();

        // 分页
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, users.size());
        if (start >= users.size()) {
            users = new ArrayList<>();
        } else {
            users = users.subList(start, end);
        }

        // 应用关联查询
        if (appends != null && !appends.isEmpty()) {
            users = applyAppends(users, appends);
        }

        // 字段过滤
        if (fields != null && !fields.isEmpty()) {
            users = applyFields(users, fields);
        }

        return new PageResult<>(users, total, page, pageSize);
    }

    public User getUserById(Long id, Map<String, Object> params) {
        if (!AuthService.canAccessUser(id)) {
            throw new SecurityException("没有访问该用户的权限");
        }

        User user = UserDao.findById(id);
        if (user == null) {
            return null;
        }

        String appends = (String) params.get("appends");
        String fields = (String) params.get("fields");

        // 应用关联查询
        if (appends != null && !appends.isEmpty()) {
            user = applyAppends(Arrays.asList(user), appends).get(0);
        }

        // 字段过滤
        if (fields != null && !fields.isEmpty()) {
            user = applyFields(Arrays.asList(user), fields).get(0);
        }

        return user;
    }

    public User createUser(User user) {
        if (!AuthService.hasPermission("CREATE_USER")) {
            throw new SecurityException("没有创建用户的权限");
        }

        // 验证用户名唯一性
        if (UserDao.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("用户名已存在");
        }

        user.setCreatedBy(AuthService.getCurrentUser().getId());
        user.setUpdatedBy(AuthService.getCurrentUser().getId());

        return UserDao.createUser(user);
    }

    public User updateUser(User user) {
        if (!AuthService.hasPermission("UPDATE_USER") && !AuthService.canAccessUser(user.getId())) {
            throw new SecurityException("没有更新该用户的权限");
        }

        User existingUser = UserDao.findById(user.getId());
        if (existingUser == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        // 检查用户名唯一性（排除当前用户）
        User userWithSameName = UserDao.findByUsername(user.getUsername());
        if (userWithSameName != null && !userWithSameName.getId().equals(user.getId())) {
            throw new IllegalArgumentException("用户名已存在");
        }

        user.setUpdatedBy(AuthService.getCurrentUser().getId());
        user.setCreatedAt(existingUser.getCreatedAt()); // 保持创建时间不变
        user.setCreatedBy(existingUser.getCreatedBy()); // 保持创建者不变

        return UserDao.updateUser(user);
    }

    public void deleteUser(Long id) {
        if (!AuthService.hasPermission("DELETE_USER")) {
            throw new SecurityException("没有删除用户的权限");
        }

        User user = UserDao.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        // 检查是否有下属用户
        List<User> subordinates = UserDao.findSubordinates(id);
        if (!subordinates.isEmpty()) {
            throw new IllegalArgumentException("该用户还有下属，无法删除");
        }

        UserDao.deleteUser(id);
    }

    private List<User> applyFilter(List<User> users, String filter) {
        // 简单实现：按用户名和昵称过滤
        String lowerFilter = filter.toLowerCase();
        return users.stream()
                .filter(user ->
                        (user.getUsername() != null && user.getUsername().toLowerCase().contains(lowerFilter)) ||
                                (user.getNickname() != null && user.getNickname().toLowerCase().contains(lowerFilter))
                )
                .collect(Collectors.toList());
    }

    private List<User> applySort(List<User> users, String sort) {
        // 简单实现：按创建时间排序
        if ("created_at".equals(sort)) {
            return users.stream()
                    .sorted(Comparator.comparing(User::getCreatedAt))
                    .collect(Collectors.toList());
        } else if ("-created_at".equals(sort)) {
            return users.stream()
                    .sorted(Comparator.comparing(User::getCreatedAt).reversed())
                    .collect(Collectors.toList());
        }
        return users;
    }

    private List<User> applyAppends(List<User> users, String appends) {
        Set<String> appendFields = new HashSet<>(Arrays.asList(appends.split(",")));

        for (User user : users) {
            if (appendFields.contains("parent") && user.getParentId() != null) {
                user.setParent(UserDao.findById(user.getParentId()));
            }
            if (appendFields.contains("children")) {
                user.setChildren(UserDao.findSubordinates(user.getId()));
            }
        }

        return users;
    }

    private List<User> applyFields(List<User> users, String fields) {
        // 简单实现：返回原对象（实际项目中可以进行字段过滤）
        return users;
    }
}
