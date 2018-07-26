package com.pphh.oauth.client.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pphh.oauth.client.ApiException;
import com.pphh.oauth.client.api.OAuth2EndpointApi;
import com.pphh.oauth.client.constant.OAuthCheckType;
import com.pphh.oauth.client.constant.TokenStoreType;
import com.pphh.oauth.client.model.ValidityVO;
import com.pphh.oauth.client.utils.CookieUtil;
import com.pphh.oauth.core.response.MessageType;
import com.pphh.oauth.core.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 检验请求中的token
 *
 * @author huangyinhuang
 * @date 7/25/2018
 */
public class OAuthSpringFilter extends OncePerRequestFilter {
    final Logger logger = LoggerFactory.getLogger(OAuthSpringFilter.class);

    private OAuthCheckType oAuthCheckType;
    private TokenStoreType tokenStoreType;
    private String tokenName;
    private String specialUrls;
    private OAuth2EndpointApi oAuthApi;

    public OAuthSpringFilter(OAuthCheckType oAuthCheckType, TokenStoreType tokenStoreType, String tokenName, String specialUrls, OAuth2EndpointApi oAuthApi) {
        this.oAuthCheckType = oAuthCheckType;
        this.tokenStoreType = tokenStoreType;
        this.tokenName = tokenName;
        this.specialUrls = specialUrls;
        this.oAuthApi = oAuthApi;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String httpUri = request.getRequestURI();
        String httpMethod = request.getMethod();

        if (this.oAuthCheckType == OAuthCheckType.ALL_CHECK_BY_SKIP) {
            // 检查所有请求，跳过特定的URL
            if (isUriSpecial(httpUri, httpMethod)) {
                // 跳过特定的URL
                filterChain.doFilter(request, response);
            } else {
                doIntrospect(request, response, filterChain);
            }

        } else if (this.oAuthCheckType == OAuthCheckType.ALL_SKIP_BY_CHECK) {

            // 跳过所有请求，检查特定的URL
            if (isUriSpecial(httpUri, httpMethod)) {
                // 检查特定的URL
                doIntrospect(request, response, filterChain);
            } else {
                filterChain.doFilter(request, response);
            }

        }
    }

    private void doIntrospect(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        Boolean isTokenValid = Boolean.FALSE;

        // 从header或cookie中获取jwt token value
        String tokenValue = null;
        if (this.tokenStoreType == TokenStoreType.COOKIE) {
            Cookie cookie = CookieUtil.getCookieByName(request, this.tokenName);
            tokenValue = (cookie != null) ? cookie.getValue() : null;
        } else if (this.tokenStoreType == TokenStoreType.HEADER) {
            tokenValue = request.getHeader(this.tokenName);
        }

        // 校验token的有效性
        if (tokenValue != null && !tokenValue.equals("null")) {
            try {
                ValidityVO validityVO = oAuthApi.introspectTokenUsingPOST(tokenValue);
                isTokenValid = validityVO.isIsValid();
            } catch (ApiException e) {
                logger.info("Receive an exception when trying to introspect the token by oauth client.", e.getMessage());
            }
        }

        // 如果token有效，则继续请求，否则返回SC_UNAUTHORIZED错误的响应
        if (isTokenValid) {
            chain.doFilter(request, response);
        } else {

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");

            String message;
            if (tokenValue == null || tokenValue.equals("null")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                message = String.format("%s为空，请先登录。", this.tokenName);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                message = String.format("%s无效，请重新登录。", this.tokenName);
            }

            Response<String> res = Response.mark(MessageType.ERROR, message);

            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().write(mapper.writeValueAsString(res));
        }
    }

    private Boolean isUriSpecial(String urlTarget, String httpMethod) {
        Boolean isSkip = false;

        String[] uriSkips = this.specialUrls.split(",");
        for (String uriSkip : uriSkips) {
            uriSkip = uriSkip.trim();

            // isMatchByEquals 是否完全匹配
            // isMatchByRegExp 是否正则匹配
            // isMatchByHttpMethod 是否请求方法相匹配，默认为true
            boolean isMatchByEquals = false;
            boolean isMatchByRegExp = false;
            boolean isMatchByHttpMethod = true;

            // 检查uriSkip是否以http方法开头
            String httpMethodRegex = "^(GET|POST|PUT|DELETE|OPTIONS|HEAD|TRACE|CONNECT|get|post|put|delete|options|head|trace|connect)&.*";
            boolean containsMethod = Pattern.matches(httpMethodRegex, uriSkip);

            // 若uriSkip包含http方法，则检查请求方法，并且获取指定的uriSkip
            // 样例 GET&^/api.*，匹配以/api开头的所有GET请求
            if (containsMethod) {
                String[] uriArray = uriSkip.split("&", 2);
                isMatchByHttpMethod = httpMethod.equals(uriArray[0].toUpperCase());
                uriSkip = uriArray[1];
            }

            isMatchByEquals = urlTarget.equals(uriSkip);
            try {
                isMatchByRegExp = Pattern.matches(uriSkip, urlTarget);
            } catch (PatternSyntaxException e) {
                logger.info("An syntax exception exists in the uri: " + uriSkip);
            }

            // if the target url matches any of skipUri, break the loop
            // otherwise continue the check with next skipUri
            isSkip = (isMatchByEquals || isMatchByRegExp) && isMatchByHttpMethod;
            if (isSkip) {
                break;
            }
        }

        return isSkip;
    }
}
