package com.cenibee.project.studygroup.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName("User 엔티티 단위 테스트")
class UserTest {

    @Test
    @DisplayName("유저 생성자(빌더) 테스트")
    void 유저_생성자_테스트() {
        //given 유저의 속성들이 주어졌을 때
        OAuth2Identifier oAuth2Identifier = mock(OAuth2Identifier.class);
        String nickname = "nickname";
        String email = "test@test.com";
        Role role = Role.ADMIN;

        //when 주어진 속성들로 유저를 생성하면
        User result = User.builder()
                .identifier(oAuth2Identifier)
                .nickname(nickname)
                .email(email)
                .role(role)
                .build();

        //then 생성된 유저에 주어진 속성이 설정되어 있음
        assertThat(result.getIdentifier()).isEqualTo(oAuth2Identifier);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getNickname()).isEqualTo(nickname);
        assertThat(result.getRole()).isEqualTo(role);
    }

}