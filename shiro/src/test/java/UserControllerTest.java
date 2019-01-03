import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.subject.WebSubject;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.http.HttpSession;

/**
 * shiro 无效咋解决？
 * 答案：测试不是用的http，是直接访问的，所以需要在test里面调用login，这样才行
 */
@RunWith(SpringRunner.class)
//@SpringApplicationConfiguration(classes = MockServletContext.class)//这个测试单个controller，不建议使用
@SpringBootTest(classes = com.wuliangzhu.shiro.Application.class)//这里的Application是springboot的启动类名。
//@WebAppConfiguration
public class UserControllerTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;
    private MockHttpSession session = new MockHttpSession();

    @Autowired
    SecurityManager securityManager;
//    @Autowired(required = true)
//    private SessionDAO sessionDAO;
    private Subject subject;
    private MockMvc mockMvc;
    private MockHttpServletRequest mockHttpServletRequest;
    private MockHttpServletResponse mockHttpServletResponse;


    @Before
    public void setUp() throws Exception {
        mockHttpServletRequest = new MockHttpServletRequest(context.getServletContext());
        mockHttpServletResponse = new MockHttpServletResponse();

        MockHttpSession mockHttpSession = new MockHttpSession(context.getServletContext());
        mockHttpServletRequest.setSession(mockHttpSession);
        SecurityUtils.setSecurityManager(securityManager);

        //       mvc = MockMvcBuilders.standaloneSetup(new TestController()).build();
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(context);//建议使用这种
//        这句话很重要，否则shiro不会生效
//        builder.addFilter((Filter)context.getBean("shiroFilter"));

        mvc = builder.build();
        this.login("wuliangzhu", "12345");
//        this.testLogin();
//        DefaultMockMvcBuilder<DefaultMockMvcBuilder<?>> builder = MockMvcBuilders.webAppContextSetup(context);
////这一句
//        builder.addFilters((Filter) context.getBean("shiroFilter"));

    }

    private void login(String username, String password) {
         subject = new WebSubject.Builder(mockHttpServletRequest, mockHttpServletResponse)
                .buildWebSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, true);
        subject.login(token);
        ThreadContext.bind(subject);
    }

    @Ignore("注册最好测试一次")
    @Test
    public void testSignin() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/signin")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("username", "wuliangzhu").param("password", "12345")
                .session(this.getMockSession())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("SUCCESS")));

    }
//    @Ignore
    @Test
    public void testLogin() throws Exception {
        MvcResult result =  mvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("username", "wuliangzhu").param("password", "12345")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("SUCCESS")))
                .andReturn();
        this.session = (MockHttpSession) result.getRequest().getSession();
        System.out.println(session.getId());
    }

//    @Test
    public void testGetLogin() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("username", "wuliangzhu").param("password", "1234")
                .session(this.getMockSession())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("SUCCESS")));

    }

//    @Test
    public void testAddRole() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/addRole")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("userId", "11").param("roleId", "1")
                .session(this.getMockSession())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("SUCCESS")));

    }

//    @Test
    public void testAddPermission() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/addPermission")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("roleId", "1").param("permissionId", "1")
//                .session(this.getMockSession())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("SUCCESS")));

    }

    @Test
    public void testGetUserInfo() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/getUserInfo")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("userName", "wuliangzhu")
//                .session(this.getMockSession())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("SUCCESS")));

    }

    /**
     * 获取登入信息session
     * @return
     * @throws Exception
     */
    private MockHttpSession getMockSession() throws Exception{
//        // mock request get login session
//        // url = /xxx/xxx/{username}/{password}
//        MvcResult result = this.mvc
//                .perform((get("/user/userMsg/loginUser/loginUser")))
//                .andExpect(status().isOk())
//                .andReturn();
//        return result.getRequest().getSession();
        System.out.println(session.getId());
        return (MockHttpSession)this.session;
    }
}
