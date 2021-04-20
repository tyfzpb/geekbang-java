package org.geektimes.oauth.config;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthConfig {

    private String clientId;

    private String clientSecret;

    private String redirectUri;

}
