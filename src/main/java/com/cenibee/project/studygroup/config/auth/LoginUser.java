package com.cenibee.project.studygroup.config.auth;

import com.cenibee.project.studygroup.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;

public class LoginUser implements OAuth2User {

    private Long id;
    private String name;
    private List<GrantedAuthority> authorities;
    private OAuth2User oAuth2User;

    @Override
    public Map<String, Object> getAttributes() {
        return ofNullable(oAuth2User).map(OAuth2User::getAttributes)
                .orElse(null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public static LoginUser of(User user, OAuth2User oAuth2User) {
        return new LoginUser(user, oAuth2User);
    }

    protected LoginUser(User user, OAuth2User oAuth2User) {
        if (user == null || oAuth2User == null) return;

        this.id = user.getId();
        this.name = user.getNickname();
        this.authorities = List.of(new SimpleGrantedAuthority(user.getRole().getRoleKey()));
        this.oAuth2User = oAuth2User;
    }

    public boolean isEmpty() {
        return getId() == null;
    }
}
