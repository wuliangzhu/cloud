package com.wuliangzhu.shiro.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 用于账号认证
 */
@Entity
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames={"userName"})})
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String userName;
    private String password;
    private String salt;

    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinTable(uniqueConstraints = {@UniqueConstraint(columnNames = {"role_id", "user_id"})})
    private List<Role> role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<Role> getRole() {
        return role;
    }

    public void setRole(List<Role> role) {
        this.role = role;
    }
}
