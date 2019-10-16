package com.example.oauth2.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * @Description: java类作用描述
 * @Author: wzl
 * @CreateDate: 2019/9/29$ 9:58$
 */

@Entity
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    private String code;

    @Column(nullable = false)
    private String name;

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }
    /** 权限点可以是任何的字符串，不一定是角色的字符串，本例权限点是从数据库中读取的Role表的nama字段*/
    @Override
    public String getAuthority() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
