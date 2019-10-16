package com.example.oauth2.filter;

import com.alibaba.fastjson.JSON;
import com.example.oauth2.model.User;
import com.example.oauth2.utils.GetRequestJsonUtils;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 自定义的用户验证过滤器
 * @Author: wzl
 * @CreateDate: 2019/10/11$ 11:05$
 */

public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        //Content-Type为json的时候验证
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)
                || request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {

            UsernamePasswordAuthenticationToken authRequest = null;
            try {
                String jsonString = GetRequestJsonUtils.getPostRequestJsonString(request);
                User user = JSON.parseObject(jsonString, User.class);
                authRequest = new UsernamePasswordAuthenticationToken(
                        user.getUsername(), user.getPassword());
            } catch (Exception e) {
                e.printStackTrace();
                authRequest = new UsernamePasswordAuthenticationToken(
                        "", "");
            } finally {
                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            }
        }
        //Content-Type为form表单时，转到UsernamePasswordAuthenticationFilter
        else {
            return super.attemptAuthentication(request, response);
        }
    }
}
