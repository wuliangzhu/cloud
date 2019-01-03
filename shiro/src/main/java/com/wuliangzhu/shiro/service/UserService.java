package com.wuliangzhu.shiro.service;

import com.wuliangzhu.shiro.model.Role;
import com.wuliangzhu.shiro.model.User;
import com.wuliangzhu.shiro.repository.RoleRepository;
import com.wuliangzhu.shiro.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Optional;

/**
 * 用来对User进行业务处理
 *
 */
@Service
public class UserService {
    @Resource
    private UserRepository repository;

    @Resource
    private RoleRepository roleRepository;

    @Transactional
    public User add(User user) {
        return this.repository.save(user);
    }

    public User getUserByName(String name) {
        return this.repository.findUserByUserName(name);
    }

    public int addRole(int userId, int roleId) {
        Optional<User> optionalUser = this.repository.findById(userId);
        User user = optionalUser.get();

        Optional<Role> optionalRole = this.roleRepository.findById(roleId);
        Role role = optionalRole.get();

        user.getRole().add(role);

        return this.repository.save(user).getId();
    }
}
