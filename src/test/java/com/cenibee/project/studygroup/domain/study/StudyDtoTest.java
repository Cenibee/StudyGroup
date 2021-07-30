package com.cenibee.project.studygroup.domain.study;

import com.cenibee.project.studygroup.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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

    @Nested
    @DisplayName("Study 리소스 DTO 유닛 테스트")
    class ResourceTest {
        @Test
        @DisplayName("엔티티에서 DTO 추출 테스트")
        void 엔티티에서_DTO_추출_테스트() {
            //given DTO 생성에 필요한 속성이 설정된 리더와 스터디가 주어졌을 때
            User leader = spy(User.builder().nickname("leader").build());
            when(leader.getId()).thenReturn(1L);

            Study study = spy(Study.builder()
                    .leader(leader)
                    .maxParticipant(5)
                    .description("desc")
                    .build());
            when(study.getId()).thenReturn(2L);
            when(study.getNumOfParticipants()).thenReturn(4);

            //when 스터디를 이용해 Resource DTO 를 생성하면
            StudyDto.Resource result = StudyDto.Resource.from(study);

            //then 주어진 리더와 스터디의 속성값이 설정된 DTO 가 반환됨
            assertThat(result.getStudyId()).isEqualTo(study.getId());
            assertThat(result.getLeaderId()).isEqualTo(leader.getId());
            assertThat(result.getLeaderNickname()).isEqualTo(leader.getNickname());
            assertThat(result.getNumOfParticipant()).isEqualTo(study.getNumOfParticipants());
            assertThat(result.getMaxParticipant()).isEqualTo(study.getMaxParticipant());
            assertThat(result.getDescription()).isEqualTo(study.getDescription());
        }
    }

}