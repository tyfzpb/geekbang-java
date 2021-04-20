package org.geektimes.oauth.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser<T> implements Serializable {

    private String uuid;

    private String userName;

    private String nickName;

    private String  avatar;

    private String blog;

    private String company;

    private String location;

    private String email;

    private String remark;

    private String source;

    private AuthToken token;

    private T rawUserInfo;
}
