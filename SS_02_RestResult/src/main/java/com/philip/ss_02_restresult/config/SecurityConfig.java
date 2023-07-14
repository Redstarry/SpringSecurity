package com.philip.ss_02_restresult.config;


import com.alibaba.fastjson2.JSON;
import com.philip.ss_02_restresult.entity.response.RestResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain mySecurity(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests()
                //.requestMatchers("/hello").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/login")
                // 登录成功
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        response.setContentType("application/json;charset=utf-8");
                        String user = request.getParameter("username");
                        String pwd = request.getParameter("password");
                        Map<String, String> dataMap = new HashMap<>();
                        dataMap.put("username", user);
                        dataMap.put("password", pwd);
                        response.getWriter().write(RestResult.success().setData(dataMap).toJsonString());

                    }
                })
                // 登录失败
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                        response.setContentType("application/json;charset=utf-8");
                        response.getWriter().write(RestResult.error(exception).toJsonString());
                    }
                })
                .and()
                .logout()
                // 登出成功
                .logoutSuccessHandler(new LogoutSuccessHandler(){
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        response.setContentType("application/json;charset=utf-8");
                        response.getWriter().write(RestResult.success().toJsonString());
                    }
                })
                .and()
                // 异常
                .exceptionHandling()
                // 未登录
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                        response.setContentType("application/json;charset=utf-8");
                        response.getWriter().write(RestResult.error(HttpServletResponse.SC_UNAUTHORIZED,"未登录").toJsonString());
                    }
                })
                // 没有权限
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        response.setContentType("application/json;charset=utf-8");
                        response.getWriter().write(RestResult.error(HttpServletResponse.SC_FORBIDDEN,"没有权限").toJsonString());
                    }
                })
                .and()
                .csrf().disable();

        return http.build();
    }
}
