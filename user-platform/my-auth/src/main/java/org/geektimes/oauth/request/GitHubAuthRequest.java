package org.geektimes.oauth.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geektimes.oauth.config.AuthConfig;
import org.geektimes.oauth.config.DefaultAuthSource;
import org.geektimes.oauth.exception.AuthException;
import org.geektimes.oauth.model.AuthCallback;
import org.geektimes.oauth.model.AuthToken;
import org.geektimes.oauth.model.AuthUser;
import org.geektimes.oauth.util.AuthUtils;
import org.geektimes.rest.util.HttpUtils;

import java.io.IOException;
import java.util.Map;

/**
 * Github登录
 *
 * @since 1.0.0
 */
public class GitHubAuthRequest extends DefaultAuthReqesut {

    public GitHubAuthRequest(AuthConfig config) {
        super(config, DefaultAuthSource.GITHUB);
    }


    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        String response = doPostAuthorizationCode(authCallback.getCode());
        Map<String, String> params = AuthUtils.parseStringToMap(response);
        this.checkResponse(params.containsKey("error"), params.get("error_description"));
        return AuthToken.builder()
                .accessToken(params.get("access_token"))
                .scope(params.get("scope"))
                .tokenType(params.get("token_type"))
                .build();

    }


    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        String url = source.userInfo();
        String userInfo = HttpUtils.get(url,"Authorization","token " + authToken.getAccessToken());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode node = objectMapper.readTree(userInfo);
            this.checkResponse(node.has("error"), node.get("error_description"));
            return AuthUser.builder()
                    .rawUserInfo(node)
                    .uuid(node.get("id").asText())
                    .userName(node.get("login").asText())
                    .avatar(node.get("avatar_url").asText())
                    .blog(node.get("blog").asText())
                    .nickName(node.get("name").asText())
                    .email(node.get("email").asText())
                    .company(node.get("company").asText())
                    .location(node.get("location").asText())
                    .remark(node.get("bio").asText())
                    .token(authToken)
                    .source(source.toString())
                    .build();
        } catch (IOException e) {
            throw new AuthException(e.getMessage());
        }

    }

    private void checkResponse(boolean error, Object errorDescription) {
        if (error) {
            throw new AuthException(errorDescription.toString());
        }
    }

}
