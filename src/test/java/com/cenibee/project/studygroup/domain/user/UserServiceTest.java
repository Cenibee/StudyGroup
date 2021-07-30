package com.cenibee.project.studygroup.domain.user;

import com.cenibee.project.studygroup.BaseMockTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.NoSuchElementException;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("UserService 단위 테스트")
class UserServiceTest extends BaseMockTest {

    @InjectMocks UserService userService;

    @Mock UserRepository userRepository;

    @Test
    @DisplayName("ID 로 단일 엔티티 조회 테스트")
    void ID_로_단일_엔티티_조회_테스트() {
        //given 가짜 영속화된 유저가 주어졌을 때
        long userId = 1L;
        User user = mock(User.class);
        when(userRepository.findById(userId)).thenReturn(of(user));

        //when ID 로 유저를 조회하면
        User result = userService.findById(userId);

        //then 주어진 가짜 유저가 반환된다.
        assertThat(result).isEqualTo(user);
    }

    @Test
    @DisplayName("ID 로 단일 엔티티 조회 테스트 - 없는 ID")
    void ID_로_단일_엔티티_조회_테스트__없는_ID() {
        //given 존재하지 않는 유저 ID 가 주어졌을 때
        long userId = 1L;
        when(userRepository.findById(1L)).thenReturn(empty());

        //expect 주어진 ID 로 유저를 조회하면 NoSuchElementException 예외가 발생한다.
        assertThatThrownBy(() -> userService.findById(userId))
                .isExactlyInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("해당 유저를 찾을 수 없습니다.");
    }

}