package org.geektimes.oauth.request;

import org.geektimes.oauth.model.AuthCallback;
import org.geektimes.oauth.model.AuthResponse;
import org.geektimes.oauth.model.AuthToken;

public interface AuthReqeust {

    /**
     * 返回带 {@code state} 参数的授权url，授权回调时会带上这个{@code state}
     *
     * @param state state 验证授权流程的参数，可以防止csrf
     * @return 返回授权地址
     */
    String authorize(String state);

    /**
     * 返回授权url，不推荐使用，建议使用
     *
     * @return 返回授权地址
     */
    @Deprecated
    String authorize();

    /**
     * 第三方登录
     *
     * @param authCallback 用于接收回调参数的实体
     * @return AuthResponse 返回登录成功后的用户信息
     */

    AuthResponse login(AuthCallback authCallback);


    /**
     * 撤销授权
     *
     * @param authToken 登录成功后返回的token信息
     * @return AuthResponse
     */
    AuthResponse revoke(AuthToken authToken);

    /**
     * 刷新 token （续期）
     *
     * @param authToken 登录成功后返回的token信息
     * @return AuthResponse
     */
    AuthResponse refresh(AuthToken authToken);


}
