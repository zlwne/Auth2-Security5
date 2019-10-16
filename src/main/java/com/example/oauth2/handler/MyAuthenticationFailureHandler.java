package com.example.oauth2.handler;

import com.alibaba.fastjson.JSON;
import com.example.oauth2.model.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 登陆失败处理器
 * @Author: wzl
 * @CreateDate: 2019/10/11$ 10:15$
 */
@Component
@Slf4j
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        ResultVO resultVO = ResultVO.error("登录失败");
        httpServletResponse.getWriter().write(JSON.toJSONString(resultVO));
    }
}
