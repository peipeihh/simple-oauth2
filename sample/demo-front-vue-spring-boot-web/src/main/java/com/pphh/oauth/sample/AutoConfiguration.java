package com.pphh.oauth.sample;

import com.pphh.oauth.client.webcontroller.OAuthClientController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Please add description here.
 *
 * @author huangyinhuang
 * @date 8/2/2018
 */
@Configuration
public class AutoConfiguration {

    @Bean
    public OAuthClientController oauthLoginController() {
        return new OAuthClientController();
    }

}
