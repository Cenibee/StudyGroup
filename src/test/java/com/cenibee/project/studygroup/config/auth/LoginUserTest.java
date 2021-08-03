package com.cenibee.project.studygroup.config.auth;

import com.cenibee.project.studygroup.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("LoginUser 유닛 테스트")
class LoginUserTest {

    @Test
    @DisplayName("로그인 유저 생성 테스트")
    void 로그인_유저_생성_테스트() {
        //given
        User user = spy(User.builder().nickname("nickname").build());
        when(user.getId()).thenReturn(1L);

        OAuth2User oAuth2User = mock(OAuth2User.class);
        when(oAuth2User.getAttributes()).thenReturn(new HashMap<>());

        //when
        LoginUser result = LoginUser.of(user, oAuth2User);

        //then
        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getName()).isEqualTo(user.getNickname());
        assertThat(result.getAttributes()).isEqualTo(oAuth2User.getAttributes());
        assertThat(result.getAuthorities())
                .anyMatch(a -> a.getAuthority().contains(user.getRole().getRoleKey()));
        assertThat(result.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("로그인 유저 생성 테스트 - 유저 null")
    void 로그인_유저_생성_테스트__유저_null() {
        //given
        OAuth2User oAuth2User = mock(OAuth2User.class);
        when(oAuth2User.getAttributes()).thenReturn(new HashMap<>());

        //when
        LoginUser result = LoginUser.of(null, oAuth2User);

        //then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("로그인 유저 생성 테스트 - OAuth2User null")
    void 로그인_유저_생성_테스트__OAuth2user_null() {
        //given
        User user = spy(User.builder().nickname("nickname").build());
        when(user.getId()).thenReturn(1L);

        //when
        LoginUser result = LoginUser.of(user, null);

        //then
        assertThat(result.isEmpty()).isTrue();
    }

}