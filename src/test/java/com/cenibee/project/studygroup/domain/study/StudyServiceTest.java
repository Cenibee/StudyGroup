package com.cenibee.project.studygroup.domain.study;

import com.cenibee.project.studygroup.BaseMockTest;
import com.cenibee.project.studygroup.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("StudyService 단위 테스트")
class StudyServiceTest extends BaseMockTest {

    @InjectMocks StudyService studyService;

    @Mock StudyRepository studyRepository;

    @Test
    @DisplayName("스터디 생성 테스트")
    void 스터디_생성_테스트() {
        //given 주어진 유저, DTO 로 변환된 엔티티와 해당 엔티티가 저장됐을 때의 식별값이 주어졌을 때
        User user = mock(User.class);

        StudyDto.ToCreate dto = spy(StudyDto.ToCreate.class);
        Study entityFromDto = mock(Study.class);
        when(dto.toEntityWith(user)).thenReturn(entityFromDto);

        long newId = 1L;
        when(studyRepository.save(entityFromDto)).thenAnswer(invocation -> {
            Study argument = invocation.getArgument(0);
            when(argument.getId()).thenReturn(newId);
            return argument;
        });

        //when 주어진 유저, DTO 로 스터디를 생성하면
        Long result = studyService.createNew(user, dto);

        //then 주어진 식별값이 반환됨
        assertThat(result).isEqualTo(newId);
    }

    @Test
    @DisplayName("ID 로 단일 스터디 조회")
    void ID_로_단일_스터디_조회() {
        //given 영속된 스터디가 주어졌을 때
        User leader = spy(User.builder().nickname("nickname").build());
        when(leader.getId()).thenReturn(1L);

        long studyId = 2L;
        Study study = spy(Study.builder().leader(leader).build());
        when(study.getId()).thenReturn(studyId);

        when(studyRepository.findById(studyId)).thenReturn(of(study));

        //when 영속 식별값으로 스터디를 조회하면
        StudyDto.Resource result = studyService.get(studyId);

        //then 주어진 스터디로 변환된 Resource DTO 가 반환됨
        assertThat(result).isEqualTo(StudyDto.Resource.from(study));
    }

    @Test
    @DisplayName("ID 로 단일 스터디 조회 - 조회 결과 없음")
    void ID_로_단일_스터디_조회__조회_결과_없음() {
        //given 유효하지 않은 식별값이 주어졌을 때
        long studyId = 1L;
        when(studyRepository.findById(studyId)).thenReturn(empty());

        //expect 주어진 식별값으로 스터디를 조회하면 NoSuchElementException 예외가 발생
        assertThatThrownBy(() -> studyService.get(studyId))
                .isExactlyInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("해당 스터디를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("전체 스터디 조회")
    void 전체_스터디_조회() {
        //given 3개의 스터디가 영속되어있을 때
        User leader = spy(User.builder().nickname("nickname").build());
        when(leader.getId()).thenReturn(1L);

        Study study1 = spy(Study.builder().leader(leader).description("desc1").build());
        when(study1.getId()).thenReturn(1L);
        Study study2 = spy(Study.builder().leader(leader).description("desc2").build());
        when(study2.getId()).thenReturn(2L);
        Study study3 = spy(Study.builder().leader(leader).description("desc3").build());
        when(study3.getId()).thenReturn(3L);
        List<Study> mockedList = List.of(study1, study2, study3);
        when(studyRepository.findAll()).thenReturn(mockedList);
                
        //when 전체 스터디를 조회하면
        List<StudyDto.Resource> result = studyService.getAll();

        //then 각 스터디가 Resource DTO 형태로 변환되어 반환됨
        assertThat(result).isEqualTo(
                mockedList.stream().map(StudyDto.Resource::from).collect(Collectors.toList()));
    }

}