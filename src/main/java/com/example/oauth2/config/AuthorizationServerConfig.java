package com.example.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: oauth2配置类
 *  认证流程：
 * （A）用户访问客户端，后者将前者导向认证服务器
 * ===>>  localhost:8888/oauth/authorize?client_id=client&response_type=code&redirect_uri=http://www.baidu.com
 * （B）用户选择是否给予客户端授权
 * （C）假设用户给予授权，认证服务器将用户导向客户端事先指定的“重定向 URI”，同时附上一个授权码
 * ===>>  www.baidu.com/?code=Yx60Ws
 * （D）客户端收到授权码，附上早先的“重定向 URI”向认证服务器申请令牌，这一步是在客户端的后台服务器上完成的，对用户不可见
 * ===>>  client:secret@localhost:8888/oauth/token
 * （E）认证服务器核对了授权码和重定向URI，确认无误后向客户端发送令牌和更新令牌
 * {
 *     "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZXN0IjoidGVzdCIsInVzZXJfbmFtZSI6InVzZXIiLCJzY29wZSI6WyJhcHAiXSwiZXhwIjoxNTcxMTMxOTEyLCJhdXRob3JpdGllcyI6WyJhZG1pbiJdLCJqdGkiOiJhMGVkYjM0ZC1jY2VhLTRjZGYtOTFlNi1mNjIyNGNmNDgwOWEiLCJjbGllbnRfaWQiOiJjbGllbnQifQ.Ij6EORxlbhzm4JaxJfVMFpp19mPnLyrtX1vGptJXjx6s6VscmNe3m-HMXlPVgpLZOedhY9Sjac_TL0xKqGlglc7E7eCC1ez5lXHJ3DTO3fJKijo9tb8S28fXJGTvYtoKNTzKlI9L5ZBg0Xs2dbwdTc_-kb77eAz6D13rrFS1tIk",
 *     "token_type": "bearer",
 *     "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZXN0IjoidGVzdCIsInVzZXJfbmFtZSI6InVzZXIiLCJzY29wZSI6WyJhcHAiXSwiYXRpIjoiYTBlZGIzNGQtY2NlYS00Y2RmLTkxZTYtZjYyMjRjZjQ4MDlhIiwiZXhwIjoxNTcxMTM0NjEyLCJhdXRob3JpdGllcyI6WyJhZG1pbiJdLCJqdGkiOiIyMjUxYzdiZi1iMGZlLTQ1OGUtYTYxNC02ZDM4ODZkYWEyYjAiLCJjbGllbnRfaWQiOiJjbGllbnQifQ.a2-ARFu0Y2P-AHqT05IB9bHkKrez2Q5ACgX5P0Ak45yLVPTfZEGmi4DeC0oKlaAbKNyT3FJrk6XdEr0YdxmkGDAXbrYcOc9q0kiDvth5FrKl31X4OMtfLT0ILBVAcksxhF34j1SAx122RCjvkuVUq193vrynY2foYN85VYnQlII",
 *     "expires_in": 299,
 *     "scope": "app",
 *     "test": "test",
 *     "jti": "a0edb34d-ccea-4cdf-91e6-f6224cf4809a"
 * }
 * （F）通过token获取资源服务器的资源，请求头带上 Authorization：bearer #{access_token}
 * @Author: wzl
 * @CreateDate: 2019/9/17$ 9:46$
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private WebSecurityConfig webSecurityConfig;

    @Autowired
    private DataSource dataSource;

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 当clientDetails使用jdbc时，客户端信息存在oauth_client_details表中
     * client_id 客户端ID
     * resource_ids
     * client_secret 客户端密码
     * scope 范围
     * authorized_grant_types 授权类型常用：authorization_code,refresh_token
     * web_server_redirect_uri 授权后访问地址
     * authorities
     * access_token_validity  token超时时间（秒）
     * refresh_token_validity  refresh_token超时时间（秒）
     * autoapprove 设true自动通过
     * @return
     */
    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * 使用私钥编码 JWT 中的  OAuth2 令牌
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "123456".toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("jwt"));
        return converter;
    }



    /**
     *使用数据库保存client信息
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetails());
    }


   @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                //允许所有资源服务器访问公钥端点（/oauth/token_key）
                //只允许验证用户访问令牌解析端点（/oauth/check_token）
                .tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()")
                // 允许客户端发送表单来进行权限认证来获取令牌
                .allowFormAuthenticationForClients();
    }


    /**
     * 告诉Spring Security Token的生成方式
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(
                Arrays.asList(tokenEnhancer(), jwtAccessTokenConverter()));

        endpoints.tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancerChain)
                .accessTokenConverter(jwtAccessTokenConverter())
                .authenticationManager(webSecurityConfig.authenticationManagerBean());
    }


    /**
     * 自定义令牌声明，添加额外的属性
     * @return
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new TokenEnhancer() {
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
                    Map<String, Object> additionalInfo = new HashMap<>();
                    additionalInfo.put("test", "test");
                    ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionalInfo);
                    return oAuth2AccessToken;
            }
        };
    }
}
