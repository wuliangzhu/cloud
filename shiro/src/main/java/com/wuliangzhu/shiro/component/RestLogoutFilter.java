package com.wuliangzhu.shiro.component;

import org.apache.shiro.web.filter.authc.LogoutFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class RestLogoutFilter extends LogoutFilter {
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        super.preHandle(request, response);
//        默认是false，这样什么也不返回，不调用controller.logout，修改为true
        return true;
    }

    @Override
    protected void issueRedirect(ServletRequest request, ServletResponse response, String redirectUrl) throws Exception {
//        防止下线之后从定向到login
    }
}
