# 参数管控系统

## 项目简介

本项目是一个集团平台参数管控系统，用于统一管理和控制各种系统参数。系统采用分层架构设计，支持用户权限管理、参数分类管理、参数值历史记录等功能。

## 技术栈

- Java 11
- Jackson (JSON处理)
- SLF4J + Logback (日志)
- Maven (构建工具)

## 功能特性

### 用户管理
- 用户登录/退出
- 用户CRUD操作
- 角色权限控制
- 用户层级管理

### 参数管理
- 参数CRUD操作
- 参数分类和分组
- 参数类型支持（布尔、整数、浮点、枚举、字符串）
- 参数值验证
- 参数历史记录

### 系统管理
- 系统信息查询
- 健康检查
- 系统统计

## API接口

### 认证接口
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/logout` - 用户退出
- `GET /api/auth/me` - 获取当前用户信息

### 用户管理接口
- `GET /api/users` - 获取用户列表
- `GET /api/users/{id}` - 获取用户详情
- `POST /api/users` - 创建用户
- `PUT /api/users/{id}` - 更新用户
- `DELETE /api/users/{id}` - 删除用户

### 参数管理接口
- `GET /api/parameters` - 获取参数列表
- `GET /api/parameters/{id}` - 获取参数详情
- `POST /api/parameters` - 创建参数
- `PUT /api/parameters/{id}` - 更新参数
- `DELETE /api/parameters/{id}` - 删除参数
- `GET /api/parameters/{id}/values` - 获取参数历史
- `GET /api/parameter-values` - 获取所有参数历史

### 系统接口
- `GET /api/system/info` - 获取系统信息
- `GET /api/system/health` - 健康检查
- `GET /api/system/stats` - 获取系统统计

## 数据模型

### 用户实体 (User)
- 基本信息：用户名、昵称、邮箱、电话
- 权限信息：角色、权限列表、状态
- 组织信息：部门、上级用户
- 时间信息：创建时间、更新时间

### 参数实体 (Parameter)
- 基本信息：名称、键、描述、类型、值
- 分类信息：分类、分组、排序
- 控制信息：是否必填、是否只读、是否可见
- 作用域：全局、部门、用户
- 验证规则：支持JSON格式的验证规则

### 参数值实体 (ParameterValue)
- 参数ID、当前值、前一个值
- 变更原因、变更时间、变更人

## 权限控制

系统采用基于角色的权限控制（RBAC），包含以下角色：

- **ADMIN（管理员）**：拥有所有权限
- **MANAGER（经理）**：可以管理下属和部门参数
- **EMPLOYEE（员工）**：只能查看相关参数

权限列表：
- `CREATE_USER`、`UPDATE_USER`、`DELETE_USER`、`VIEW_USER`
- `CREATE_PARAMETER`、`UPDATE_PARAMETER`、`DELETE_PARAMETER`、`VIEW_PARAMETER`
- `VIEW_SYSTEM_STATS`

## 部署说明

### 环境要求
- JDK 11+
- Maven 3.6+

### 编译运行
```bash
# 编译项目
mvn clean compile

# 运行项目
mvn exec:java -Dexec.mainClass="Application"

# 打包项目
mvn clean package
