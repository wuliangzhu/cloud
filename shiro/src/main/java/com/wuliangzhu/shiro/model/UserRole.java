package com.wuliangzhu.shiro.model;

import javax.persistence.*;

/**
 * 角色权限关系表
 */
//@Entity
//@Table(name = "user_role", uniqueConstraints = {@UniqueConstraint(columnNames={"user_id", "role_id"}, name = "u_r")})
public class UserRole {
    @Id
    @GeneratedValue
    private int id;
    private int userId;
    private int roleId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
