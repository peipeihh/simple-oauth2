package com.pphh.oauth.config;

import com.pphh.oauth.client.api.OAuth2EndpointApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * 自动化配置oauth api client
 *
 * @author huangyinhuang
 * @date 7/25/2018
 */
public class ClientAutoConfiguration {

    @Value("${oauth.server.url:http://localhost:8090}")
    private String authServerUrl;

    @Bean
    @ConditionalOnProperty(name = "oauth.server.url")
    public OAuth2EndpointApi oAuth2EndpointApi() {
        OAuth2EndpointApi oAuth2EndpointApi = new OAuth2EndpointApi();
        oAuth2EndpointApi.getApiClient().setBasePath(authServerUrl);
        return oAuth2EndpointApi;
    }

}
