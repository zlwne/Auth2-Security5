package com.example.oauth2.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: java类作用描述
 * @Author: wzl
 * @CreateDate: 2019/9/27$ 16:42$
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('admin')")
    public String getHello() {
        return "hello,I'm Iron Man,Require user privileges";
    }

    @GetMapping("/hi")
    @PreAuthorize("hasAuthority('user')")
    public String getHi() {
        return "hi,I'm Iron Man,No privileges";
    }

}
