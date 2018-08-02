package com.pphh.oauth.client.webcontroller;

import com.pphh.oauth.client.ApiException;
import com.pphh.oauth.client.api.OAuth2EndpointApi;
import com.pphh.oauth.client.model.OAuth2AccessToken;
import com.pphh.oauth.core.response.MessageType;
import com.pphh.oauth.core.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Please add description here.
 *
 * @author huangyinhuang
 * @date 7/26/2018
 */
@RestController
@RequestMapping("/api/oauth2")
public class OAuthClientController {

    final Logger log = LoggerFactory.getLogger(OAuthClientController.class);

    @Value("${oauth.server.url:}")
    private String authServerUrl;

    @Value("${oauth.client.callback:}")
    private String defaultClientCallbackUrl;

    @Value("${oauth.client.id:}")
    private String clientId;

    @Value("${oauth.client.authorization:}")
    private String authorization;

    @Autowired
    private OAuth2EndpointApi oAuthApi;

    @RequestMapping(value = "/redirectUrl", method = RequestMethod.GET)
    public Response fetchRedirectUrl(@RequestParam(value = "callback", required = false) String clientCallbackUrl) {
        String callback = clientCallbackUrl == null ? defaultClientCallbackUrl : clientCallbackUrl;
        if (authServerUrl != null && callback != null && clientId != null) {
            try {
                callback = URLEncoder.encode(callback, "utf-8");
            } catch (UnsupportedEncodingException e) {
                log.info(e.getMessage(), e);
            }
            String redirectUrl = String.format(
                    "%s/#/authorize?response_type=code&client_id=%s&redirect_uri=%s&scope=user_role&state=1",
                    authServerUrl, clientId, callback);
            return Response.mark(MessageType.SUCCESS, redirectUrl);
        } else {
            return Response.mark(MessageType.ERROR, "获取重定向登录地址失败，请检查配置项是否完整。");
        }
    }

    @RequestMapping(value = "/accessToken", method = RequestMethod.GET)
    public Response exchangeAccessToken(@RequestParam(value = "code", required = true) String code) {

        OAuth2AccessToken oAuth2AccessToken = null;
        if (code != null && this.authorization != null) {
            try {
                oAuth2AccessToken = oAuthApi.issueTokenUsingPOST("AUTHORIZATION_CODE",
                        this.authorization, code, this.defaultClientCallbackUrl, null, null, null);
            } catch (ApiException e) {
                log.info("调用oAuthApi获取token发现异常。" + e.getResponseBody(), e);
            }
        }

        if (oAuth2AccessToken != null) {
            return Response.mark(MessageType.SUCCESS, oAuth2AccessToken);
        } else {
            return Response.mark(MessageType.ERROR, "获取token失败，请确保参数正确以及code有效。");
        }

    }

    @RequestMapping(value = "/refreshToken", method = RequestMethod.GET)
    public Response refreshAccessToken(@RequestParam(value = "refresh_token", required = true) String refreshToken) {

        OAuth2AccessToken oAuth2AccessToken = null;
        if (refreshToken != null && this.authorization != null) {
            try {
                oAuth2AccessToken = oAuthApi.issueTokenUsingPOST("REFRESH_TOKEN",
                        this.authorization, null, this.defaultClientCallbackUrl, refreshToken, null, null);
            } catch (ApiException e) {
                log.info("调用oAuthApi刷新token发现异常。" + e.getResponseBody(), e);
            }
        }

        if (oAuth2AccessToken != null) {
            return Response.mark(MessageType.SUCCESS, oAuth2AccessToken);
        } else {
            return Response.mark(MessageType.ERROR, "刷新token失败，请确保参数正确以及refreshToken有效。");
        }

    }

    @RequestMapping(value = "/revokeToken", method = RequestMethod.GET)
    public Response revokeAccessToken(@RequestParam(value = "token", required = true) String token) {
        Boolean bSuccess = Boolean.FALSE;
        if (token != null && this.authorization != null) {
            try {
                bSuccess = oAuthApi.revokeTokenUsingPOST(token, this.authorization);
            } catch (ApiException e) {
                log.info("调用oAuthApi吊销token发现异常。" + e.getResponseBody(), e);
            }
        }

        if (bSuccess) {
            return Response.mark(MessageType.SUCCESS, "token吊销成功。");
        } else {
            return Response.mark(MessageType.ERROR, "token吊销失败，请确保参数以及token正确。");
        }
    }
}
