package com.wuliangzhu.shiro.controller;

import com.wuliangzhu.shiro.model.User;
import com.wuliangzhu.shiro.service.RoleService;
import com.wuliangzhu.shiro.service.UserService;
import com.wuliangzhu.shiro.util.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.bytebuddy.asm.Advice;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 处理用户登录问题
 * 只要没有验证，都会从定向到get的 login请求上
 */
@RestController
public class UserController extends BaseController{
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;

    @PostMapping(value = "/signin")
    public JsonResult register(String username, String password) {
        User user = new User();

        user.setUserName(username);
        user.setPassword(password);

        User ret = this.userService.add(user);

        return JsonResult.success("add SUCCESS:" + ret.getId());
    }

    @GetMapping(value = "/login")
    public JsonResult login(HttpServletRequest request) {
        return JsonResult.fail("fail").addResult("tip", "验证失败，请重新登录");
    }

    /**
     * 参数一定要是 username password 都是小写的
     *
     * @param request
     * @param username
     * @param password
     * @return
     */
    @ApiOperation(value = "用户登录")
    @PostMapping(value = "/login")
    public JsonResult login(HttpServletRequest request, String username, String password) {
            return this.login(request, Map.of("username", username, "password", password));
    }

//    @ApiOperation(value = "用户登录")
//    @PostMapping(value = "/login")
    public JsonResult login(HttpServletRequest request, Map<String, Object> map) {
        System.out.println("HomeController.login2");
        // 登录失败从request中获取shiro处理的异常信息
        // shiroLoginFailure:就是shiro异常类的全类名
        String exception = (String) request.getAttribute("shiroLoginFailure");
        String msg = "";
        if (exception != null) {
            if (UnknownAccountException.class.getName().equals(exception)) {
                System.out.println("UnknownAccountException -->帐号不存在：");
                msg = "UnknownAccountException -->帐号不存在：";
            } else if (IncorrectCredentialsException.class.getName().equals(exception)) {
                System.out.println("IncorrectCredentialsException -- > 密码不正确：");
                msg = "IncorrectCredentialsException -- > 密码不正确：";
            } else if ("kaptchaValidateFailed".equals(exception)) {
                System.out.println("kaptchaValidateFailed -- > 验证码错误");
                msg = "kaptchaValidateFailed -- > 验证码错误";
            } else {
                msg = "else >> " + exception;
                System.out.println("else -- >" + exception);
            }
        }
//        map.put("msg", msg);

        // 此方法不处理登录成功,由shiro进行处理.
        if (msg.length() > 0) {
            return JsonResult.fail("fail").addResult("result", msg);
        }
        else {
            return JsonResult.success("SUCCESS").addResult("result", "登录成功");
        }
    }

    /**
     * 给用户添加权限
     *
     * @return
     */
    @ApiOperation(value = "添加角色")
    @PostMapping(value = "/addRole")
    public JsonResult addRole(int userId, int roleId) {
        int retId = this.userService.addRole(userId, roleId);

        return JsonResult.success("add role SUCCESS" + retId);
    }

    @ApiOperation(value = "添加权限")
    @PostMapping(value = "/addPermission")
    public JsonResult addPermission(int roleId, int permissionId) {
        int retId = this.roleService.addPermisson(roleId, permissionId);

        return JsonResult.success("add permission SUCCESS:" + retId);
    }

    @ApiOperation(value = "获得用户信息")
    @GetMapping(value = "/getUserInfo")
    public JsonResult getUserInfo(String userName) {
        User user = this.userService.getUserByName(userName);

        return JsonResult.success("SUCCESS").addResult("user", user);
    }

    @ApiOperation(value = "当前用户下线")
    @PostMapping(value = "/logout")
    public JsonResult logout() {
        Subject subject = SecurityUtils.getSubject();

        logger.info("user logout:" + subject.getPrincipal());

        return JsonResult.success("下线成功");
    }
}
