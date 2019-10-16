package com.example.oauth2.model.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultVO implements Serializable {

    /**
     * 操作成功默认code
     */
    public static final int DEFAULT_SUCCESS_CODE = 0;
    /**
     * 操作失败默认code
     */
    public static final int DEFAULT_ERROR_CODE = -1;

    /**
     * 操作成功默认msg
     */
    private static final String DEFAULT_SUCCESS_MSG = "操作成功";
    /**
     * 操作失败默认msg
     */
    private static final String DEFAULT_ERROR_MSG = "操作失败";

    private int code;

    private String msg;

    /**
     * 用于接收任意类型数据，便于返回到前台
     */
    private Object data;



    public static ResultVO ok() {
        return ResultVO.builder().code(DEFAULT_SUCCESS_CODE).msg(DEFAULT_SUCCESS_MSG).build();
    }

    public static ResultVO ok(String msg) {
        return ResultVO.builder().code(DEFAULT_SUCCESS_CODE).msg(msg).build();
    }

    public static ResultVO ok(Object data) {
        return ResultVO.builder().code(DEFAULT_SUCCESS_CODE).msg(DEFAULT_SUCCESS_MSG).data(data).build();
    }


    public static ResultVO error(String msg, int code) {
        return ResultVO.builder().code(code).msg(msg).build();
    }

    public static ResultVO error(String msg) {
        return ResultVO.builder().code(DEFAULT_ERROR_CODE).msg(msg).build();
    }

    public static ResultVO error() {
        return ResultVO.builder().code(DEFAULT_ERROR_CODE).msg(DEFAULT_ERROR_MSG).build();
    }


}
