package com.example.oauth2.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;

/**
 * @Description: 获取 post 请求 request 中的 json数据工具类
 * @Author: wzl
 * @CreateDate: 2019/10/11$ 13:55$
 */

public class GetRequestJsonUtils {

    /**
     * 返回 json  字符串
     * @param request
     * @return
     */
    public static String getPostRequestJsonString(HttpServletRequest request) {
        BufferedReader br;
        StringBuilder sb = null;
        String jsonString = null;
        try {
            br = new BufferedReader(new InputStreamReader(
                    request.getInputStream()));
            String line = null;
            sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            jsonString = URLDecoder.decode(sb.toString(), "UTF-8");
            jsonString = jsonString.substring(jsonString.indexOf("{"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
