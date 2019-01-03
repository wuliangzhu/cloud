package com.wuliangzhu.shiro.interceptor;

import com.wuliangzhu.shiro.util.JsonResult;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 处理全局的异常问题
 */
@ControllerAdvice
@ResponseBody
public class ExceptionInterceptor {

    @ExceptionHandler
    public JsonResult handleCommonException(AuthorizationException authorizationException) {
        String msg = authorizationException.getMessage();

        return JsonResult.fail(msg);
    }
}
