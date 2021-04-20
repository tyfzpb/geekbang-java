package org.geektimes.oauth.request;

import org.geektimes.oauth.config.AuthConfig;
import org.geektimes.oauth.config.AuthSource;
import org.geektimes.oauth.exception.AuthException;
import org.geektimes.oauth.model.AuthCallback;
import org.geektimes.oauth.model.AuthResponse;
import org.geektimes.oauth.model.AuthToken;
import org.geektimes.oauth.model.AuthUser;
import org.geektimes.rest.util.HttpUtils;
import org.geektimes.util.UuidUtils;

public abstract class DefaultAuthReqesut implements AuthReqeust {

    protected AuthConfig config;
    protected AuthSource source;


    public DefaultAuthReqesut(AuthConfig config, AuthSource source) {
        this.config = config;
        this.source = source;
    }

    @Override
    public String authorize(String state) {
        StringBuilder stringBuilder = new StringBuilder(source.authorize());
        stringBuilder.append("?").append("response_type=code")
                .append("&client_id=").append(config.getClientId())
                .append("&redirect_uri=").append(config.getRedirectUri())
                .append("&state=").append(getRealState(state));
        return stringBuilder.toString();
    }

    /**
     * 获取state，如果为空， 则默认取当前日期的时间戳
     *
     * @param state 原始的state
     * @return 返回不为null的state
     */
    protected String getRealState(String state) {
        if (state == null || state.isEmpty()) {
            state = UuidUtils.getUUID();
        }
        return state;
    }

    @Override
    public String authorize() {
        return this.authorize(null);
    }

    @Override
    public AuthResponse login(AuthCallback authCallback) {
        try {
            AuthToken authToken = this.getAccessToken(authCallback);
            AuthUser authUser = this.getUserInfo(authToken);
            return AuthResponse.builder().code(200).data(authUser).build();
        } catch (Exception e) {
            return responseError(e);
        }
    }

    /**
     * 发生异常的情况，统一响应参数
     *
     * @param e
     * @return
     */
    private AuthResponse responseError(Exception e) {
        int errorCode = 500;
        String errorMsg = e.getMessage();
        if (e instanceof AuthException) {
            AuthException authException = ((AuthException) e);
            errorCode = authException.getErrorCode();
            String error = authException.getErrorMsg();
            if (error == null || error.trim().equals("")) {
                errorMsg = error;
            }
        }
        return AuthResponse.builder().code(errorCode).msg(errorMsg).build();
    }

    /**
     * 获取用户信息
     *
     * @param authToken token信息
     * @return 用户信息
     */
    protected abstract AuthUser getUserInfo(AuthToken authToken);

    /**
     * 获取access token
     *
     * @param authCallback 授权成功后的回调参数
     * @return token
     */
    protected abstract AuthToken getAccessToken(AuthCallback authCallback);

    @Override
    public AuthResponse revoke(AuthToken authToken) {
        return null;
    }

    @Override
    public AuthResponse refresh(AuthToken authToken) {
        return null;
    }

    /**
     * 返回获取accessToken的url
     *
     * @param code 授权码
     * @return 返回获取accessToken的url
     */
    protected String accessTokenUrl(String code) {
        StringBuilder stringBuilder = new StringBuilder(source.accessToken());
        stringBuilder.append("?code=").append(code)
                .append("&client_id=").append(config.getClientId())
                .append("&client_secret=").append(config.getClientSecret())
                .append("&grant_type=").append("authorization_code")
                .append("&redirect_uri=").append(config.getRedirectUri());
        return stringBuilder.toString();
    }

    /**
     * 返回获取accessToken的url
     *
     * @param refreshToken refreshToken
     * @return 返回获取accessToken的url
     */
    protected String refreshTokenUrl(String refreshToken) {
        StringBuilder stringBuilder = new StringBuilder(source.refresh());
        stringBuilder.append("?client_id=").append(config.getClientId())
                .append("&client_secret=").append(config.getClientSecret())
                .append("&refresh_token=").append(refreshToken)
                .append("&grant_type=").append("authorization_code")
                .append("&redirect_uri=").append(config.getRedirectUri());
        return stringBuilder.toString();
    }

    /**
     * 返回获取userInfo的url
     *
     * @param authToken token
     * @return 返回获取userInfo的url
     */
    protected String userInfoUrl(AuthToken authToken) {
        StringBuilder stringBuilder = new StringBuilder(source.userInfo());
        stringBuilder.append("?access_token=").append(authToken.getAccessToken());
        return stringBuilder.toString();
    }

    /**
     * 返回获取revoke authorization的url
     *
     * @param authToken token
     * @return 返回获取revoke authorization的url
     */
    protected String revokeUrl(AuthToken authToken) {
        StringBuilder stringBuilder = new StringBuilder(source.revoke());
        stringBuilder.append("?access_token=").append(authToken.getAccessToken());
        return stringBuilder.toString();
    }

    /**
     * 通用的 authorizationCode 协议
     *
     * @param code code码
     * @return Response
     */
    protected String doPostAuthorizationCode(String code) {
        return HttpUtils.post(accessTokenUrl(code));
    }

    /**
     * 通用的 authorizationCode 协议
     *
     * @param code code码
     * @return Response
     */
    protected String doGetAuthorizationCode(String code) {
        return HttpUtils.get(accessTokenUrl(code));
    }

    /**
     * 通用的 用户信息
     *
     * @param authToken token封装
     * @return Response
     */
    @Deprecated
    protected String doPostUserInfo(AuthToken authToken) {
        return HttpUtils.post(userInfoUrl(authToken));
    }

    /**
     * 通用的 用户信息
     *
     * @param authToken token封装
     * @return Response
     */
    protected String doGetUserInfo(AuthToken authToken) {
        return HttpUtils.get(userInfoUrl(authToken));
    }

    /**
     * 通用的post形式的取消授权方法
     *
     * @param authToken token封装
     * @return Response
     */
    @Deprecated
    protected String doPostRevoke(AuthToken authToken) {
        return HttpUtils.post(revokeUrl(authToken));
    }

    /**
     * 通用的post形式的取消授权方法
     *
     * @param authToken token封装
     * @return Response
     */
    protected String doGetRevoke(AuthToken authToken) {
        return HttpUtils.get(revokeUrl(authToken));
    }

}
