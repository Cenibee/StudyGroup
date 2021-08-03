package com.cenibee.project.studygroup.config;

import com.cenibee.project.studygroup.config.auth.LoginUser;
import com.cenibee.project.studygroup.domain.user.OAuth2Identifier;
import com.cenibee.project.studygroup.domain.user.User;
import com.cenibee.project.studygroup.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomOauth2UserService implements OAuth2UserService<OAuth2UserRequest, LoginUser> {

    private static final DefaultOAuth2UserService DELEGATE = new DefaultOAuth2UserService();
    private final UserService userService;

    @Override
    public LoginUser loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = DELEGATE.loadUser(userRequest);

        ClientRegistration registration = userRequest.getClientRegistration();
        String registrationId = registration.getRegistrationId();
        String endpointIdKey = registration.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        String endpointId = oAuth2User.getAttribute(endpointIdKey);

        OAuth2Identifier identifier = OAuth2Identifier.of(registrationId, oAuth2User.getAttribute(endpointId));

        User loginUser = userService.createOrGet(identifier);
        checkEmail(loginUser, oAuth2User);

        return LoginUser.of(loginUser, oAuth2User);
    }

    private void checkEmail(User loginUser, OAuth2User oAuth2User) {

    }

}
