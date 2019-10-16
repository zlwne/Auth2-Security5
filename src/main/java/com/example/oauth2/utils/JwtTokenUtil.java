package com.example.oauth2.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;

/**
 * @Description: JWT生产和校验类
 * 使用keytool生成密钥，别名为 jwt，算法为RSA，有效期为 365天，文件名为jwt.jks,
 * 把文件保存在当前打开cmd的路径下,它提示输入密码,输入自定义密码，当前我设置为 123456 ，
 * 接下的输入可以全部忽略，回车即可，最后输入 y 确定，把生成的文件复制到resources目录下
 * cmd命令： keytool -genkey -alias jwt -keyalg  RSA -keysize 1024 -validity 365 -keystore jwt.jks
 * @Author: wzl
 * @CreateDate: 2019/10/11$ 15:15$
 */

public class JwtTokenUtil {

    /**
     * 寻找证书文件
     */
    private static InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("jwt.jks");
    private static PrivateKey privateKey = null;
    private static PublicKey publicKey = null;

    static { // 将证书文件里边的私钥公钥拿出来
        try {
            // java key store 固定常量
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(inputStream, "123456".toCharArray());
            // jwt 为 命令生成整数文件时的别名
            privateKey = (PrivateKey) keyStore.getKey("jwt", "123456".toCharArray());
            publicKey = keyStore.getCertificate("jwt").getPublicKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用私钥加密 token
     * @param subject
     * @param expirationSeconds
     * @return
     */
    public static String generateToken(String subject, int expirationSeconds) {
        return Jwts.builder()
                .setClaims(null)
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + expirationSeconds * 1000))
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    /**
     * 不使用公钥私钥 加密token
     * @param subject
     * @param expirationSeconds
     * @param salt
     * @return
     */
    public static String generateToken(String subject, int expirationSeconds, String salt) {
        return Jwts.builder()
                .setClaims(null)
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + expirationSeconds * 1000))
                // 不使用公钥私钥
                .signWith(SignatureAlgorithm.HS512, salt)
                .compact();
    }

    /**
     * 通过 公钥解密token
     * @param token
     * @return
     */
    public static Map<String, Object> parseToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }

    /**
     * 不通过 公钥解密token
     * @param token
     * @param salt
     * @return
     */
    public static String parseToken(String token,String salt) {
        String subject = null;
        try {
            Claims claims = Jwts.parser()
                    // 不使用公钥私钥
                    .setSigningKey(salt)
                    .parseClaimsJws(token).getBody();
            subject = claims.getSubject();
        } catch (Exception e) {
        }
        return subject;
    }
}
