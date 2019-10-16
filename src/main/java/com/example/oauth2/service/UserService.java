package com.example.oauth2.service;

import com.example.oauth2.mapper.UserMapper;
import com.example.oauth2.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: wzl
 * @CreateDate: 2019/9/29$ 10:27$
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> queryUserByuserName(User user){
        return userMapper.queryUserByuserName(user);
    }

    public User insertUser(String username,String passWord){
        User user = new User();
        user.setPassword(passWord);
        user.setUsername(username);
        userMapper.insert(user);
        return user;
    }

    public List<User> list(User user){
        return userMapper.list(user);
    }


}
