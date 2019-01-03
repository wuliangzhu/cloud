package com.wuliangzhu.shiro.model;

import javax.persistence.*;

/**
 * 角色权限表
 */
////@Entity
//@Table(name = "permission",
//        uniqueConstraints = {@UniqueConstraint(name = "r_p", columnNames = {"role_id", "permission_id"})})
public class RolePermission {
    @Id
    @GeneratedValue
    private int id;
    private int roleId;
    private int permissionId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }
}
