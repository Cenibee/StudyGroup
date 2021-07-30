package com.cenibee.project.studygroup.domain.study;

import com.cenibee.project.studygroup.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName("StudyDTO 유닛테스트")
class StudyDtoTest {

    @Nested
    @DisplayName("Study 생성 DTO 유닛 테스트")
    class ToCreateTest {
        @Test
        @DisplayName("Study 엔티티 변환 테스트")
        void Study_엔티티_변환_테스트() {
            //given 리더가 될 유저와 toCreate DTO 와 그 속성들이 주어졌을 때
            User leader = mock(User.class);
            int maxParticipant = 3;
            String description = "desc";
            StudyDto.ToCreate dto = new StudyDto.ToCreate(maxParticipant, description);

            //when 주어진 유저를 이용해 엔티티로 변환하면
            Study result = dto.toEntityWith(leader);

            //then 주어진 유저가 리더고, 주어진 속성들을 가지는 Study 엔티티가 반환됨
            assertThat(result).isNotNull();
            assertThat(result.getLeader()).isEqualTo(leader);
            assertThat(result.getMaxParticipant()).isEqualTo(maxParticipant);
            assertThat(result.getDescription()).isEqualTo(description);
        }
    }

}