package com.pphh.oauth.config;

import com.pphh.oauth.client.api.OAuth2EndpointApi;
import com.pphh.oauth.client.constant.OAuthCheckType;
import com.pphh.oauth.client.constant.TokenStoreType;
import com.pphh.oauth.client.filter.OAuthSpringFilter;
import com.pphh.oauth.client.filter.UserInfoFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 自动化配置spring web过滤器
 *
 * @author huangyinhuang
 * @date 7/25/2018
 */
public class FilterAutoConfiguration {
    final Logger logger = LoggerFactory.getLogger(FilterAutoConfiguration.class);

    @Value("${oauth.spring.filter.type:all-check-by-skip}")
    private String oauthFilterType;

    @Value("${oauth.spring.filter.token.store.type:header}")
    private String tokenStoreType;

    @Value("${oauth.spring.filter.token.name:oauth-token}")
    private String tokenName;

    @Value("${oauth.spring.filter.special.urls:}")
    private String specialUrls;

    @Value("${oauth.spring.filter.audit.userinfo:audit-userinfo}")
    private String auditUserInfo;

    @Bean
    @ConditionalOnProperty(name = "oauth.spring.filter.type", havingValue = "all-check-by-skip")
    public OncePerRequestFilter oAuthSpringFilter1(OAuth2EndpointApi oAuthApi) {
        return buildOAuthSpringFilter(oAuthApi, OAuthCheckType.ALL_CHECK_BY_SKIP);
    }

    @Bean
    @ConditionalOnProperty(name = "oauth.spring.filter.type", havingValue = "all-skip-by-check")
    public OncePerRequestFilter oAuthSpringFilter2(OAuth2EndpointApi oAuthApi) {
        return buildOAuthSpringFilter(oAuthApi, OAuthCheckType.ALL_SKIP_BY_CHECK);
    }

    @Bean
    @ConditionalOnProperty(name = "oauth.spring.filter.audit.userinfo")
    public OncePerRequestFilter userInfoFilter() {
        TokenStoreType storeType = parseTokenType();
        return new UserInfoFilter(storeType, this.tokenName, this.auditUserInfo);
    }

    private OncePerRequestFilter buildOAuthSpringFilter(OAuth2EndpointApi oAuthApi, OAuthCheckType oAuthCheckType) {
        TokenStoreType storeType = parseTokenType();
        return new OAuthSpringFilter(oAuthCheckType, storeType, tokenName, specialUrls, oAuthApi);
    }

    private TokenStoreType parseTokenType() {
        TokenStoreType storeType = TokenStoreType.COOKIE;

        try {
            storeType = TokenStoreType.valueOf(this.tokenStoreType.toUpperCase());
        } catch (IllegalArgumentException exception) {
            logger.info("The token store type is invalid, please look at valid type name in the enum class - TokenStoreType");
        }

        return storeType;
    }

}
