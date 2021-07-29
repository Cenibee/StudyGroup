package com.cenibee.project.studygroup.domain.study;

import com.cenibee.project.studygroup.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName("Participate 엔티티 단위 테스트")
class ParticipateTest {

    @Test
    @DisplayName("정적 팩터리 메서드 테스트")
    void 정적_팩터리_메서드_테스트() {
        //given 유저와 스터디가 주어졌을 때
        User user = mock(User.class);
        Study study = mock(Study.class);

        //when 유저를 스터디에 가입시키면
        Participate result = Participate.of(user, study);

        //then 엔티티는 주어진 유저와 스터디를 갖는다.
        assertThat(result.getParticipant()).isEqualTo(user);
        assertThat(result.getStudy()).isEqualTo(study);
    }

}