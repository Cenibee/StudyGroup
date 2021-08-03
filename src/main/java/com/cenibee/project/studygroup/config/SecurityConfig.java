package com.cenibee.project.studygroup.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final OAuth2UserService oauth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(a -> a
                        .anyRequest().authenticated())
                .oauth2Login(o -> o
                        .userInfoEndpoint().userService(oauth2UserService))
                .logout(l -> l
                        .logoutSuccessUrl("/api"))
                .csrf().disable();
    }
}
