package com.wuliangzhu.shiro.service;

import com.wuliangzhu.shiro.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PermissionService {
    @Resource
    private PermissionRepository permissionRepository;
}
