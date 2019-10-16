package com.example.oauth2.common;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @Description: java类作用描述
 * @Author: wzl
 * @CreateDate: 2019/9/29$ 10:18$
 */
public interface SuperMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
