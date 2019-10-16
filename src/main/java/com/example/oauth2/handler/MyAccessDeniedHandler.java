package com.example.oauth2.handler;

import com.alibaba.fastjson.JSON;
import com.example.oauth2.model.vo.ResultVO;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 无权限处理类
 * @Author: wzl
 * @CreateDate: 2019/10/11$ 14:50$
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        ResultVO resultVO = ResultVO.error("权限不足");
        httpServletResponse.getWriter().write(JSON.toJSONString(resultVO));
    }
}
