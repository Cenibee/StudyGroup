package com.cenibee.project.studygroup.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("User DTO 테스트")
class UserDtoTest {

    @Nested
    @DisplayName("ToUpdate 단위 테스트")
    class ToUpdateTest {

        @Test
        @DisplayName("유저 업데이트 테스트")
        void 유저_업데이트_테스트() {
            //given 식별 값이 동일한 DTO 와 유저가 주어졌을 때
            long userId = 1L;
            UserDto.ToUpdate dto = new UserDto.ToUpdate(userId, "test@email.com", "nickname");
            User user = mock(User.class);
            when(user.getId()).thenReturn(userId);

            //when 주어진 DTO 로 유저를 업데이트 하면
            dto.update(user);

            //then 주어진 DTO 의 속성으로 유저 업데이트 메서드가 호출됨
            verify(user).update(dto.getNickname(), dto.getEmail());
        }

        @Test
        @DisplayName("유저 업데이트 테스트 - ID 불일치")
        void 유저_업데이트_테스트__ID_불일치() {
            //given 식별 값이 다른 DTO 와 유저가 주어졌을 때
            User user = mock(User.class);
            when(user.getId()).thenReturn(1L);
            UserDto.ToUpdate dto = new UserDto.ToUpdate(2L, "test@email.com", "nickname");

            //expect 주어진 DTO 로 유저를 업데이트 하면 IllegalArgumentException 예외가 발생
            assertThatThrownBy(() -> dto.update(user))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("id 불일치로 유저 정보를 업데이트 할 수 없습니다.");
        }

    }

}