package org.geektimes.spring.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@EnableWebSecurity
@Configuration
public class DefaultWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    private List<IWebSecurityConfigurer> webSecurityConfigurerList ;


    protected  void configure(HttpSecurity httpSecurity)throws Exception{
        for (IWebSecurityConfigurer webSecurityConfigurer : webSecurityConfigurerList){
            webSecurityConfigurer.configure(httpSecurity);
        }
    }


    @Autowired(required = false)
    public void setWebSecurityConfigurerList(@Value("#{@getAutoWriredIWebSecurityConfigurers}")List<IWebSecurityConfigurer> webSecurityConfigurerList){
        Collections.sort(webSecurityConfigurerList, AnnotationAwareOrderComparator.INSTANCE);
        this.webSecurityConfigurerList = webSecurityConfigurerList;
    }

    @Bean
    public List<IWebSecurityConfigurer> getAutoWriredIWebSecurityConfigurers(ConfigurableListableBeanFactory beanFactory){
        List<IWebSecurityConfigurer> webSecurityConfigurers = new ArrayList<>();
        Map<String, IWebSecurityConfigurer> beansOfType = beanFactory
                .getBeansOfType(IWebSecurityConfigurer.class);
        for (Map.Entry<String, IWebSecurityConfigurer> entry : beansOfType.entrySet()) {
            webSecurityConfigurers.add(entry.getValue());
        }
        return webSecurityConfigurers;
    }



}
