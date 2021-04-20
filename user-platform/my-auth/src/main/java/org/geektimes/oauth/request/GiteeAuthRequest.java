package org.geektimes.oauth.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geektimes.oauth.config.AuthConfig;
import org.geektimes.oauth.config.DefaultAuthSource;
import org.geektimes.oauth.exception.AuthException;
import org.geektimes.oauth.model.AuthCallback;
import org.geektimes.oauth.model.AuthToken;
import org.geektimes.oauth.model.AuthUser;
import org.geektimes.rest.util.HttpUtils;

import java.io.IOException;

/**
 * Gitee登录
 *
 * @since 1.0.0
 */
public class GiteeAuthRequest extends DefaultAuthReqesut {

    public GiteeAuthRequest(AuthConfig config) {
        super(config, DefaultAuthSource.GITEE);
    }


    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        String response = doPostAuthorizationCode(authCallback.getCode());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode node = objectMapper.readTree(response);
            checkResponse(node);
            return AuthToken.builder()
                    .accessToken(node.get("access_token").asText())
                    .refreshToken(node.get("refresh_token").asText())
                    .scope(node.get("scope").asText())
                    .tokenType(node.get("token_type").asText())
                    .expireIn(node.get("expires_in").asInt())
                    .build();
        } catch (IOException e) {
            throw new AuthException(e.getMessage());
        }
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        String userInfo = doGetUserInfo(authToken);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode node = objectMapper.readTree(userInfo);
            checkResponse(node);
            return AuthUser.builder()
                    .rawUserInfo(node)
                    .uuid(node.get("id").asText())
                    .userName(node.get("login").asText())
                    .avatar(node.get("avatar_url").asText())
                    .blog(node.get("blog").asText())
                    .nickName(node.get("name").asText())
                    .email(node.get("email").asText())
                    .remark(node.get("bio").asText())
                    .token(authToken)
                    .source(source.toString())
                    .build();
        } catch (IOException e) {
            throw new AuthException(e.getMessage());
        }
    }

    /**
     * 检查响应内容是否正确
     *
     * @param object 请求响应内容
     */
    private void checkResponse(JsonNode object) {
        if (object.has("error")) {
            throw new AuthException(object.get("error_description").toString());
        }
    }

}
