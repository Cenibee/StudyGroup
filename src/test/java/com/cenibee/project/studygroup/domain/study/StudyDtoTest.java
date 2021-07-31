package com.cenibee.project.studygroup.domain.study;

import com.cenibee.project.studygroup.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Nested
    @DisplayName("Study 업데이트 DTO 유닛 테스트")
    class ToUpdateTest {
        @Test
        @DisplayName("엔티티 업데이트 테스트")
        void 엔티티_업데이트_테스트() {
            //given ID 가 일치하는 스터디와 업데이트 DTO 가 주어졌을 때
            long studyId = 1L;
            Study study = mock(Study.class);
            when(study.getId()).thenReturn(studyId);

            int maxParticipant = 3;
            String description = "desc";
            StudyDto.ToUpdate dto = new StudyDto.ToUpdate(studyId, maxParticipant, description);

            //when DTO 로 스터디를 업데이트하면
            dto.update(study);

            //then DTO 의 속성 값으로 스터디의 update() 함수가 실행됨
            verify(study).update(maxParticipant, description);
        }

        @Test
        @DisplayName("엔티티 업데이트 테스트 - ID 가 일치하지 않는 경우")
        void 엔티티_업데이트_테스트__ID_가_일치하지_않는_경우() {
            //given ID 가 일치하지 않는 스터디와 업데이트 DTO 가 주어졌을 때
            long studyId = 1L;
            Study study = mock(Study.class);
            when(study.getId()).thenReturn(studyId);

            int maxParticipant = 3;
            String description = "desc";
            StudyDto.ToUpdate dto = new StudyDto.ToUpdate(2L, maxParticipant, description);

            //when DTO 로 스터디를 업데이트하면 IllegalArgumentException 예외가 발생한다.
            assertThatThrownBy(() -> dto.update(study))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("id 불일치로 스터디 정보를 업데이트 할 수 없습니다.");
        }
    }

}