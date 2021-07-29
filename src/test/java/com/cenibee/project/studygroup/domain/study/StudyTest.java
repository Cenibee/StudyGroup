package com.cenibee.project.studygroup.domain.study;

import com.cenibee.project.studygroup.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

@DisplayName("Study 엔티티 단위 테스트")
public class StudyTest {

    @Test
    @DisplayName("스터디원 인원수 조회 테스트")
    void 스터디원_인원수_조회_테스트() {
        //given 스터디가 주어지고, 그 스터디에 1 명이 더 참가했을 때
        Study study = Study.builder().leader(mock(User.class)).maxParticipant(3).build();
        study.join(mock(User.class));

        //when 참가한 스터디원을 조회하면
        int result = study.getNumOfParticipants();

        //then 2명의 스터디원이 있는 것을 조회
        assertThat(result).isEqualTo(2);
    }

    @Test
    @DisplayName("스터디원 참가 여부 조회 테스트")
    void 스터디원_참가_여부_조회_테스트() {
        //given 스터디, 스터디에 참가 유저가 주어졌을 때
        User user = mock(User.class);
        Study study = Study.builder().leader(mock(User.class)).maxParticipant(3).build();
        study.join(user);

        //when 스터디에 참가한 유저가 존재하는지 확인하면
        boolean result = study.existsUser(user);

        //then 정상적으로 참가되어 있음
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("스터디원 참가 여부 조회 테스트 - 참가하지 않은 유저")
    void 스터디원_참가_여부_조회_테스트__참가하지_않은_유저() {
        //given 스터디, 스터디에 참가하지 않은 유저가 주어졌을 때
        User user = mock(User.class);
        Study study = Study.builder().leader(mock(User.class)).maxParticipant(3).build();

        //when 유저가 존재하는지 확인하면
        boolean result = study.existsUser(user);

        //then 참가되어 있지 않음
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("스터디원 참가 여부 조회 테스트 - 유저가 null")
    void 스터디원_참가_여부_조회_테스트__유저가_null() {
        //given 스터디가 주어졌을 때
        Study study = Study.builder().leader(mock(User.class)).maxParticipant(3).build();

        //when null 유저가 존재하는지 확인하면
        boolean result = study.existsUser(null);

        //then 참가되어 있지 않음
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("유저 참가하기 테스트")
    void 유저_참가하기_테스트() {
        //given 스터디와 유저가 주어졌을 때
        User user = mock(User.class);
        Study study = Study.builder().leader(mock(User.class)).maxParticipant(3).build();

        //when 유저가 스터디에 참가하면
        Participate result = study.join(user);

        //then 주어진 스터디와 유저의 참가가 반환됨 / 스터디에 주어진 유저가 존재
        assertThat(result.getStudy()).isEqualTo(study);
        assertThat(result.getParticipant()).isEqualTo(user);
        assertThat(study.existsUser(user)).isTrue();
    }

    @Test
    @DisplayName("유저 참가하기 테스트 - 이미 참가한 유저 (멱등성)")
    void 유저_참가하기_테스트__이미_참가한_유저() {
        //given 스터디, 스터디에 참가 유저와 그 참가 정보가 주어졌을 때
        User user = mock(User.class);
        Study study = Study.builder().leader(mock(User.class)).maxParticipant(3).build();
        Participate joined = study.join(user);
        int size = study.getNumOfParticipants();

        //when 이미 참가한 유저가 스터디에 참가하면
        Participate result = study.join(user);

        //then 주어진 참가 정보가 반환됨 / 스터디에 기존 유저가 존재 / 참가자 수가 불변
        assertThat(result).isEqualTo(joined);
        assertThat(study.existsUser(user)).isTrue();
        assertThat(study.getNumOfParticipants()).isEqualTo(size);
    }

    @Test
    @DisplayName("유저 참가하기 테스트 - 인원 제한에 걸리는 경우")
    void 유저_참가하기_테스트__인원_제한에_걸리는_경우() {
        //given 스터디 참가자가 가득 찬 study 와 참가하지 않은 유저가 주어졌을 때
        Study study = Study.builder().leader(mock(User.class)).maxParticipant(2).build();
        study.join(mock(User.class));

        User newParticipant = mock(User.class);

        //expect 유저를 스터디에 참가시키면 IllegalStateException 예외가 발생
        assertThatThrownBy(() -> study.join(newParticipant))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("더 이상 참가할 수 없습니다.");
    }

    @Test
    @DisplayName("유저 참가하기 테스트 - 유저가 null")
    void 유저_참가하기_테스트__유저가_null() {
        //given 스터디가 주어졌을 때
        Study study = Study.builder().maxParticipant(3).build();

        //when null 유저가 스터디에 참가하면 IllegalArgumentException 예외가 발생
        assertThatThrownBy(() -> study.join(null))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("유저가 null 입니다.");
    }

    @Test
    @DisplayName("유저 탈퇴하기 테스트")
    void 유저_탈퇴하기_테스트() {
        //given 스터디와 스터디에 참가한 유저가 주어졌을 때
        User user = mock(User.class);
        Study study = Study.builder().leader(mock(User.class)).maxParticipant(3).build();
        study.join(user);

        //when 유저가 스터디에서 탈퇴하면
        boolean result = study.remove(user);

        //then 삭제에 성공 / 스터디에 주어진 유저가 존재하지 않음
        assertThat(result).isTrue();
        assertThat(study.existsUser(user)).isFalse();
    }

    @Test
    @DisplayName("유저 탈퇴하기 테스트 - 리더 탈퇴 불가")
    void 유저_탈퇴하기_테스트__리더_탈퇴_불가() {
        //given 스터디와 스터디 리더가 주어졌을 때
        User user = mock(User.class);
        Study study = Study.builder().leader(user).build();

        //when 스터디 리더가 스터디를 탈퇴하면
        boolean result = study.remove(user);

        //then 삭제에 실패 / 리더가 여전히 존재
        assertThat(result).isFalse();
        assertThat(study.existsUser(user)).isTrue();
    }

    @Test
    @DisplayName("유저 탈퇴하기 테스트 - 참가하지 않은 유저")
    void 유저_탈퇴하기_테스트__참가하지_않은_유저() {
        //given 스터디와 스터디에 참가하지 않은 유저가 주어졌을 때
        User user = mock(User.class);
        Study study = Study.builder().leader(mock(User.class)).maxParticipant(3).build();

        //when 유저가 스터디에서 탈퇴하면
        boolean result = study.remove(user);

        //then 삭제에 실패
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("유저 탈퇴하기 테스트 - 유저가 null")
    void 유저_탈퇴하기_테스트__유저가_null() {
        //given 스터디가 주어졌을 때
        Study study = Study.builder().leader(mock(User.class)).maxParticipant(3).build();

        //when null 유저가 스터디에서 탈퇴하면
        boolean result = study.remove(null);

        //then 삭제에 실패
        assertThat(result).isFalse();
    }

}
