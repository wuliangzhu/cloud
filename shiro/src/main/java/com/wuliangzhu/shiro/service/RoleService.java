package com.wuliangzhu.shiro.service;

import com.wuliangzhu.shiro.model.Permission;
import com.wuliangzhu.shiro.model.Role;
import com.wuliangzhu.shiro.repository.PermissionRepository;
import com.wuliangzhu.shiro.repository.RoleRepository;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class RoleService {
    @Resource
    private RoleRepository roleRepository;
    @Resource
    private PermissionRepository permissionRepository;

    @RequiresPermissions(value = "user:add:addview")
    public int addPermisson(int roleId, int permissionId) {
        Optional<Permission> permission = this.permissionRepository.findById(permissionId);

        Optional<Role> role = this.roleRepository.findById(roleId);
        role.get().getPermission().add(permission.get());

        return this.roleRepository.save(role.get()).getId();
    }
}
