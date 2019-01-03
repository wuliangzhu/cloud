package com.wuliangzhu.shiro.component;

import com.wuliangzhu.shiro.model.Role;
import com.wuliangzhu.shiro.model.User;
import com.wuliangzhu.shiro.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

/**
 * 基础的根据账号密码验证
 * 根据账号查出权限
 */
public class SimpleRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        principals.forEach( e -> {
            String name = e.toString();
            User user = this.userService.getUserByName(name);

            List<Role> roleList = user.getRole();
            roleList.forEach( r -> {
                authorizationInfo.addRole(r.getName());

                // add permission
                List<com.wuliangzhu.shiro.model.Permission> permissionList = r.getPermission();
                permissionList.forEach( p -> {
                    StringBuffer sb = new StringBuffer();

                    sb.append(p.getResource());
                    sb.append(":");
                    sb.append(isEmpty(p.getOperator()) ? "*" : p.getOperator());
                    sb.append(":");
                    sb.append(isEmpty(p.getInstanceId()) ? "*" : p.getInstanceId());

                    authorizationInfo.addObjectPermission(
                            new WildcardPermission(sb.toString()));
                });
            });
        });

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = token.getPrincipal().toString();
        String password = token.getCredentials().toString();

        User user = this.userService.getUserByName(username);
        if (user == null)
            throw new UnknownAccountException();

        return new SimpleAuthenticationInfo(username, user.getPassword(), username);
    }

    private static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
