package org.geektimes.projects.user.web.controller;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.geektimes.oauth.config.AuthConfig;
import org.geektimes.oauth.model.AuthCallback;
import org.geektimes.oauth.request.AuthReqeust;
import org.geektimes.oauth.request.GitHubAuthRequest;
import org.geektimes.oauth.request.GiteeAuthRequest;
import org.geektimes.web.mvc.controller.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import java.io.IOException;

public class Oauth2Controller implements RestController {


    @Path("/login/gitee")
    public void loginGitee(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String result = getAuthReqeust("gitee").authorize();
        response.sendRedirect(result);
    }

    @Path("/login/oauth")
    public Object oauth(String code,String source,String state){
        AuthReqeust authReqeust = getAuthReqeust(source);
        return authReqeust.login(AuthCallback.builder().code(code).state(state).build());
    }



    @Path("/login/github")
    public void loginGithub(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String result = getAuthReqeust("github").authorize();
        response.sendRedirect(result);
    }


    private AuthReqeust getAuthReqeust(String name) {
        Config config = ConfigProviderResolver.instance().getConfig();
        String clientId = config.getValue(name + ".client_id", String.class);
        String clientSecret = config.getValue(name + ".client_secret", String.class);
        String redirectUri = config.getValue(name + ".redirect_uri", String.class);
        if ("gitee".equals(name)) {
            return new GiteeAuthRequest(
                    AuthConfig.builder().clientId(clientId)
                            .clientSecret(clientSecret)
                            .redirectUri(redirectUri)
                            .build());
        }
        return new GitHubAuthRequest(
                AuthConfig.builder().clientId(clientId)
                        .clientSecret(clientSecret)
                        .redirectUri(redirectUri)
                        .build());
    }

}
