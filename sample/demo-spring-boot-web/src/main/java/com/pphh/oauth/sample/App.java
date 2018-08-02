package com.pphh.oauth.sample;

import com.pphh.oauth.client.ApiException;
import com.pphh.oauth.client.api.OAuth2EndpointApi;
import com.pphh.oauth.client.model.OAuth2AccessToken;
import com.pphh.oauth.client.utils.CookieUtil;
import com.pphh.oauth.core.constant.GrantType;
import com.pphh.oauth.core.identity.Identity;
import com.pphh.oauth.core.utils.BasicAuthUtil;
import com.pphh.oauth.core.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 一个演示样例，通过resource owner credential向授权服务器获取访问令牌，并存储在web cookie中
 *
 * @author huangyinhuang
 * @date 8/1/2018
 */
@SpringBootApplication
@RestController
@ComponentScan(basePackages = {"com.pphh.oauth"})
public class App {

    final Logger logger = LoggerFactory.getLogger(App.class);

    @Value("${oauth.spring.filter.token.name:oauth-token}")
    private String tokenName;
    @Value("${oauth.client.id:}")
    private String clientId;
    @Value("${oauth.client.secret:}")
    private String clientSecret;

    @Autowired
    private OAuth2EndpointApi oAuthApi;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    /**
     * 通过cookie检查是否用户登录成功
     *
     * @param request 前端请求
     * @return 返回当前登录用户信息，或者返回错误
     */
    @GetMapping("/user")
    public ResponseEntity<String> greet(HttpServletRequest request) {
        String msg = null;
        HttpStatus status = null;

        Cookie cookie = CookieUtil.getCookieByName(request, tokenName);
        if (cookie != null) {
            String token = cookie.getValue();
            Identity identity = JwtUtil.decode(token);
            if (identity != null) {
                status = HttpStatus.OK;
                msg = String.format("当前登录用户是：%s", identity.getName());
            }
        }

        if (status == null) {
            msg = "对不起，没有发现登录的用户，请点击登录按钮。";
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(msg, status);
    }

    /**
     * 通过resource owner credential向授权服务器获取访问令牌，并存储在web cookie中
     *
     * @param credential 用户的登录名和密码
     * @param response   前端响应
     * @return 返回当前获取登录授权的信息，若失败，返回错误消息
     */
    @PostMapping(value = "/api/login")
    public ResponseEntity<String> login(@RequestBody UserCredential credential,
                                        HttpServletResponse response) {
        String msg = null;
        HttpStatus status = null;

        OAuth2AccessToken accessToken = null;
        try {
            String authorization = BasicAuthUtil.encode(clientId, clientSecret);
            accessToken = oAuthApi.issueTokenUsingPOST(GrantType.PASSWORD.name(), authorization, null, null, null, credential.getUserName(), credential.getUserPwd());
        } catch (ApiException e) {
            logger.error("receive an exception when trying to fetch access/refresh token with resource owner grant way", e.getMessage());
        }

        if (accessToken != null) {
            // set the cookie with the token
            CookieUtil.setCookie(response, this.tokenName, accessToken.getAccessToken(), AppProperties.cookieDomain,
                    AppProperties.cookiePath, AppProperties.cookieSecure, AppProperties.cookieExpiry);
            msg = "已成功登录，授权信息已存储到浏览器cookie中。";
            status = HttpStatus.OK;
        } else {
            msg = String.format("登录失败，请确认输入的用户名和密码正确，或者检查登录授权服务器%s是否在运行。", oAuthApi.getApiClient().getBasePath());
            status = HttpStatus.FORBIDDEN;
        }

        return new ResponseEntity<>(msg, status);
    }

    /**
     * 清除web中的登录cookie信息
     *
     * @param response 前端响应
     * @return 返回清除操作结果信息
     */
    @PostMapping(value = "/api/logout")
    public String logout(HttpServletResponse response) {
        // remove the cookie by setting the expiry as 0
        CookieUtil.setCookie(response, tokenName, "", AppProperties.cookieDomain,
                AppProperties.cookiePath, AppProperties.cookieSecure, 0);

        return "已成功登录，浏览器cookie中的授权信息已全部被删除。";
    }

}
