package org.geektimes.spring.security.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 *
 */
public interface IWebSecurityConfigurer {

    void configure(HttpSecurity httpSecurity) throws Exception;

}
