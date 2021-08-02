package com.cenibee.project.studygroup.domain.user;

import com.cenibee.project.studygroup.BaseMockTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.NoSuchElementException;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    @DisplayName("OAuth2Identifier 로 유저 생성 or 조회 - 생성")
    void OAuth2Identifier_로_유저_생성_or_조회__생성() {
        //given 주어진 identifier 로 조회되는 유저가 없고, save() 함수가 스텁되어 있을 때
        OAuth2Identifier identifier = mock(OAuth2Identifier.class);
        when(userRepository.findByIdentifier(identifier)).thenReturn(empty());
        when(userRepository.save(any())).thenAnswer(returnsFirstArg());

        //when 주어진 identifier 로 유저 생성 or 조회 호출하면
        User result = userService.createOrGet(identifier);

        //then 결과 유저는 save() 되었음 / 결과는 주어진 identifier 를 갖음
        verify(userRepository).save(result);
        assertThat(result.getIdentifier()).isEqualTo(identifier);
    }

    @Test
    @DisplayName("OAuth2Identifier 로 유저 생성 or 조회 - 조회")
    void OAuth2Identifier_로_유저_생성_or_조회__조회() {
        //given 주어진 identifier 로 죄회되는 유저가 주어졌을 때
        OAuth2Identifier identifier = mock(OAuth2Identifier.class);
        User user = mock(User.class);
        when(userRepository.findByIdentifier(identifier)).thenReturn(of(user));

        //when 주어진 identifier 로 유저 생성 or 조회 호출하면
        User result = userService.createOrGet(identifier);

        //then 주어진 유저가 반환됨
        assertThat(result).isEqualTo(user);
    }

    @Test
    @DisplayName("유저 정보 업데이트")
    void 유저_정보_업데이트() {
        //given 영속된 유저와 업데이트 DTO 가 주어졌을 때
        long userId = 1L;
        User user = mock(User.class);
        when(userRepository.findById(userId)).thenReturn(of(user));

        UserDto.ToUpdate dto = mock(UserDto.ToUpdate.class);
        when(dto.getUserId()).thenReturn(userId);

        //when 주어진 유저 식별값과 DTO 로 업데이트하면
        userService.update(userId, dto);

        //then DTO 가 유저를 업데이트함
        verify(dto).update(user);
    }

    @Test
    @DisplayName("유저 정보 업데이트 - 예외 발생")
    void 유저_정보_업데이트__예외_발생() {
        //given 영속된 유저와 업데이트 DTO 가 주어지고, DTO 에서 업데이트 시 예외가 발생하면
        long userId = 1L;
        User user = mock(User.class);
        when(userRepository.findById(userId)).thenReturn(of(user));

        UserDto.ToUpdate dto = mock(UserDto.ToUpdate.class);
        when(dto.getUserId()).thenReturn(userId);

        RuntimeException ex = mock(RuntimeException.class);
        doThrow(ex).when(dto).update(any());

        //expect 주어진 유저 식별값과 DTO 로 업데이트할 때 예외가 그대로 전이됨
        assertThatThrownBy(() -> userService.update(userId, dto))
                .isEqualTo(ex);
    }

    @Test
    @DisplayName("유저 정보 업데이트 - 다른 유저")
    void 유저_정보_업데이트__다른_유저() {
        //given 주어진 유저 식별값과 다른 식별자를 가지는 DTO 가 주어졌을 때
        long userId = 1L;

        UserDto.ToUpdate dto = mock(UserDto.ToUpdate.class);
        when(dto.getUserId()).thenReturn(2L);

        //expect 주어진 유저 식별값과 DTO 로 업데이트할 때 IllegalStateException 예외 발생
        assertThatThrownBy(() -> userService.update(userId, dto))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("유저 관리 권한이 없습니다.");
    }


}