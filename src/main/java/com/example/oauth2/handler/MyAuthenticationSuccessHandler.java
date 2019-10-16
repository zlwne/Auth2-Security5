package com.example.oauth2.handler;

import com.alibaba.fastjson.JSON;
import com.example.oauth2.model.User;
import com.example.oauth2.model.vo.AuthMsg;
import com.example.oauth2.model.vo.ResultVO;
import com.example.oauth2.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 登陆成功处理器
 * @Author: wzl
 * @CreateDate: 2019/10/11$ 10:04$
 */
@Component
@Slf4j
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        ResultVO resultVO = ResultVO.ok("登陆成功");
        User user = (User) authentication.getPrincipal();
        //创建 token并返回，设置过期时间为 300 秒
        String jwtToken = JwtTokenUtil.generateToken(user.getUsername(), 300);
        AuthMsg authMsg = new AuthMsg();
        authMsg.setToken(jwtToken);
        resultVO.setData(authMsg);
        httpServletResponse.getWriter().write(JSON.toJSONString(resultVO));
    }
}
