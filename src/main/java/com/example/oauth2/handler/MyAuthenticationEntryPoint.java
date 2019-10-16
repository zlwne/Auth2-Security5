package com.example.oauth2.handler;

import com.alibaba.fastjson.JSON;
import com.example.oauth2.model.vo.ResultVO;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 未登录时处理
 * @Author: wzl
 * @CreateDate: 2019/10/11$ 14:57$
 */
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        ResultVO resultVO = ResultVO.error("请进行登录");
        httpServletResponse.getWriter().write(JSON.toJSONString(resultVO));
    }
}
