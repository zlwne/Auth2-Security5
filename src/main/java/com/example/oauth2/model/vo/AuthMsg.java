package com.example.oauth2.model.vo;

import lombok.Data;

/**
 * @Description: java类作用描述
 * @Author: wzl
 * @CreateDate: 2019/10/11$ 15:27$
 */

@Data
public class AuthMsg {

    private String token;

    private String refreshToken;
}
