package com.wuliangzhu.shiro.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 单个权限 query
 * 单个资源多个权限 user:query user:add 多值 user:query,add
 * 单个资源所有权限 user:query,add,update,delete user:*
 * 所有资源某个权限 *:view
 * 实例级别的权限控制
 * 单个实例的单个权限 printer:query:lp7200 printer:print:epsoncolor
 * 所有实例的单个权限 printer:print:*
 * 所有实例的所有权限 printer:*:*
 * 单个实例的所有权限 printer:*:lp7200
 * 单个实例的多个权限 printer:query,print:lp7200
 * 字符串通配符权限
 *     规则：“资源标识符：操作：对象实例ID”  即对哪个资源的哪个实例可以进行什么操作。其默认支持通配符权限字符串，
 *     “:”表示资源/操作/实例的分割；“,”表示操作的分割；“*”表示任意资源/操作/实例。
 */
@Entity
@Table(name = "permission")
public class Permission {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String description;

    /**
     * 要保护的资源
     */
    private String resource;
    /**
     * 对资源进行的操作
     */
    private String operator;
    /**
     * 资源的特例
     */
    private String instanceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
