package com.pphh.oauth.client.constant;

/**
 * 过滤的检查类型
 *
 * @author huangyinhuang
 * @date 4/2/2018
 */
public enum OAuthCheckType {

    /**
     * 检查所有请求，跳过特定的URL
     */
    ALL_CHECK_BY_SKIP,

    /**
     * 跳过所有请求，检查特定的URL
     */
    ALL_SKIP_BY_CHECK,

}
