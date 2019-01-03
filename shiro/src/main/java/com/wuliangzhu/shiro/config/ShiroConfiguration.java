package com.wuliangzhu.shiro.config;

import com.github.streamone.shiro.session.RedissonSessionDao;
import com.github.streamone.shiro.session.RedissonWebSessionManager;
import com.wuliangzhu.shiro.component.CredentialMatcher;
import com.wuliangzhu.shiro.component.RestAuthenticationFilter;
import com.wuliangzhu.shiro.component.RestLogoutFilter;
import com.wuliangzhu.shiro.component.SimpleRealm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {
    /**
     * ip:port 是否启用redisSession，如果不启用用的就是tomcat的session机制
     */
    @Value("shiro.session.enable")
    private boolean enable = true;
    @Value("shiro.session.redis")
    private String shiroRedis = "127.0.0.1:6379";
    @Value("shiro.session.password")
    private String password = "Chris6612520";

    /**
     * 配置shiro过滤器
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        //1.定义shiroFactoryBean
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //2.设置securityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //3.LinkedHashMap是有序的，进行顺序拦截器配置
        Map<String,String> filterChainMap = new LinkedHashMap<String,String>();

        //4.配置logout过滤器
        filterChainMap.put("/logout", "logout");

        //5.所有url必须通过认证才可以访问
        filterChainMap.put("/login", "login");
        filterChainMap.put("/logout", "restLogout");
        filterChainMap.put("/signin", "anon");
        filterChainMap.put("/swagger*", "anon");
        filterChainMap.put("/swagger-ui.html", "anon");
        filterChainMap.put("/swagger-resources/**", "anon");
        filterChainMap.put("/v2/api-docs", "anon");
        filterChainMap.put("/webjars/springfox-swagger-ui/**", "anon");
        filterChainMap.put("/**","authc");

        //6.设置默认登录的url
        shiroFilterFactoryBean.setLoginUrl("/login");

        //7.设置成功之后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/index");

        //8.设置未授权界面
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

        //9.设置custom filter
        Map<String, Filter> filterMap = shiroFilterFactoryBean.getFilters();
        filterMap.put("login", new RestAuthenticationFilter());
        filterMap.put("restLogout", new RestLogoutFilter());
//        shiroFilterFactoryBean.setFilters(filterMap);


        //10.设置shiroFilterFactoryBean的FilterChainDefinitionMap
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainMap);

        return shiroFilterFactoryBean;
    }
    /**
     * 配置安全管理器
     * 1
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(simpleRealm());

        if (enable) {
            this.customSessionManager(securityManager);
        }

        return securityManager;
    }

    /**
     * 利用Redsession进行分布式回话保存
     *
     * @param securityManager
     */
    public void customSessionManager(DefaultWebSecurityManager securityManager) {
        // Create a RedissonClient object as a redis client
        Config redissonCfg = new Config();
        redissonCfg.useSingleServer()
                .setAddress("redis://" + (this.shiroRedis == null? "" : this.shiroRedis))
                .setPassword(password);

        RedissonClient redisson = Redisson.create(redissonCfg);

        RedissonSessionDao sessionDao = new RedissonSessionDao();
        sessionDao.setRedisson(redisson);
        // Optional, the default codec is JsonJacksonCodec
        sessionDao.setCodec(new JsonJacksonCodec());
        RedissonWebSessionManager sessionManager = new RedissonWebSessionManager();
        sessionManager.setSessionDAO(sessionDao);
//        sessionManager.setSessionIdCookie(sessionIdCookie);
        sessionManager.setGlobalSessionTimeout(1800000);

        securityManager.setSessionManager(sessionManager);
    }
    @Bean
    public SimpleRealm simpleRealm(){
        SimpleRealm realm = new SimpleRealm();
        realm.setCredentialsMatcher(credentialMatcher());

        return realm;
    }

    @Bean
    public CredentialMatcher credentialMatcher() {
        return new CredentialMatcher();
    }

    /**
     * 解决代码中使用 shiro 注解的问题
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);

        return advisorAutoProxyCreator;
    }

    /**
     * 让代码中可以使用shiro注解
     *
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());

        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 用来解决@configutation 不能读取 yml文件的配置问题
     * @return
     */
    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
