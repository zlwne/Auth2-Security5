package com.example.oauth2.config;


import com.example.oauth2.filter.JwtAuthenticationTokenFilter;
import com.example.oauth2.filter.MyUsernamePasswordAuthenticationFilter;
import com.example.oauth2.handler.*;
import com.example.oauth2.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * @Description: security5配置类
 * @Author: wzl
 * @CreateDate: 2019/9/27$ 13:39$
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private MyPasswordEncoder myPasswordEncoder;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Autowired
    private MyAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private MyAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(myPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //开启登录
                .formLogin().permitAll()
                .and()
                //登出
                .logout().logoutSuccessUrl("/").logoutSuccessHandler(myLogoutSuccessHandler)
                //用户注册的接口不需要校验
                .and().authorizeRequests().antMatchers("/user/register").permitAll()
                //其他接口需要校验登录
                .anyRequest().authenticated()
                //记住我
                //.and().rememberMe().rememberMeParameter("remember-me").userDetailsService(userDetailsService)
                //token持久化
                //.tokenRepository(persistentTokenRepository())
                //.tokenValiditySeconds(60 * 60)
                //权限异常情况的处理（当采用表单登录时authenticationEntryPoint需注释掉）
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler)//.authenticationEntryPoint(authenticationEntryPoint)
                .and()
                //如果不禁用的话会默认把post请求拦截下来
                .csrf().disable()
                //设置自定义的用户校验
                .addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

    }


    /**
     * 注册自定义的UsernamePasswordAuthenticationFilter
     * @return
     * @throws Exception
     */
    @Bean
    public MyUsernamePasswordAuthenticationFilter customAuthenticationFilter() throws Exception {
        MyUsernamePasswordAuthenticationFilter filter = new MyUsernamePasswordAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
        // 设置自定义登陆接口名
        filter.setFilterProcessesUrl("/userLogin");
        //这句很关键，重用WebSecurityConfigurerAdapter配置的AuthenticationManager，不然要自己组装AuthenticationManager
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }




    /**
     * 持久化token(使用remember-me使用)
     * Security中，默认是使用PersistentTokenRepository的子类InMemoryTokenRepositoryImpl，将token放在内存中
     * 如果使用JdbcTokenRepositoryImpl，会创建表persistent_logins，将token持久化到数据库
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        // 设置数据源
        tokenRepository.setDataSource(dataSource);
        // 启动创建表，创建成功后注释掉
        //tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

}
