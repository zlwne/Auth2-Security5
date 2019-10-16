package com.example.oauth2.filter;

import com.example.oauth2.service.MyUserDetailsService;
import com.example.oauth2.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** OncePerRequestFilter 确保在一次请求只通过一次filter，而不需要重复执行
 * @Description: JWT令牌认证过滤器
 * @Author: wzl
 * @CreateDate: 2019/10/11$ 15:09$
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private static String BEAR = "bearer ";
    private static String AUTHORIZATION = "Authorization";

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = httpServletRequest.getHeader(AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith(BEAR)) {
            final String authToken = authHeader.substring(BEAR.length());

            String username = (String) JwtTokenUtil.parseToken(authToken).get("user_name");

            if (username != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
