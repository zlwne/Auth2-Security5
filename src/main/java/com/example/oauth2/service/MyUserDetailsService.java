package com.example.oauth2.service;

import com.example.oauth2.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: wzl
 * @CreateDate: 2019/9/27$ 17:05$
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = new User();
        user.setUsername(userName);
        List<User> userList = userService.queryUserByuserName(user);
        if (CollectionUtils.isEmpty(userList)) {
            throw new RuntimeException("error.user.not.exist");
        }
        return userService.queryUserByuserName(user).get(0);
    }
}
