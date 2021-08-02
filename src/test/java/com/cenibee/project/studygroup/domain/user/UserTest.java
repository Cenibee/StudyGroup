package com.cenibee.project.studygroup.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @ParameterizedTest(name = "기존 닉네임 : {0}, 기존 이메일 : {1}")
    @CsvSource({",", "nickname,org@email.com"})
    @DisplayName("유저 업데이트 테스트 - 기존 속성 nullable")
    void 유저_업데이트_테스트__기존_속성_nullable(String setNickname, String setEmail) {
        //given 업데이트할 닉네임과 이메일이 주어졌을 때
        String nickname = "newNickname";
        String email = "new@email.com";
        User user = User.builder().nickname(setNickname).email(setEmail).build();

        //when 주어진 속성으로 유저를 업데이트하면
        user.update(nickname, email);

        //then 유저에 주어진 속성들이 설정됨
        assertThat(user.getNickname()).isEqualTo(nickname);
        assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("유저 업데이트 테스트 - 이메일 nullable")
    void 유저_업데이트_테스트__이메일_nullable() {
        //given 업데이트할 닉네임만 주어졌을 때
        String nickname = "newNickname";
        String existsEmail = "org@email.com";
        User user = User.builder().nickname("nickname").email(existsEmail).build();

        //when 주어진 닉네임과 null 이메일로 유저를 업데이트하면
        user.update(nickname, null);

        //then 닉네임만 업데이트 됨
        assertThat(user.getNickname()).isEqualTo(nickname);
        assertThat(user.getEmail()).isEqualTo(existsEmail);
    }

    @Test
    @DisplayName("유저 업데이트 테스트 - 닉네임 nullable")
    void 유저_업데이트_테스트__닉네임_nullable() {
        //given 업데이트할 이메일만 주어졌을 때
        String email = "new@email.com";
        String existsNickname = "nickname";
        User user = User.builder().nickname(existsNickname).email("org@email.com").build();

        //when 주어진 이메일과 null 닉네임으로 유저를 업데이트하면
        user.update(null, email);

        //then 이메일만 업데이트 됨
        assertThat(user.getNickname()).isEqualTo(existsNickname);
        assertThat(user.getEmail()).isEqualTo(email);
    }

}