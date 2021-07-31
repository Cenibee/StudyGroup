package com.cenibee.project.studygroup.domain.study;

import com.cenibee.project.studygroup.BaseMockTest;
import com.cenibee.project.studygroup.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void 서비스_스파이() {
        studyService = spy(studyService);
    }

    @Test
    @DisplayName("스터디 생성 테스트")
    void 스터디_생성_테스트() {
        //given 주어진 유저, DTO 로 변환된 엔티티와 해당 엔티티가 저장됐을 때의 식별값이 주어졌을 때
        User user = mock(User.class);

        StudyDto.ToCreate dto = mock(StudyDto.ToCreate.class);
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
    @DisplayName("스터디 생성 테스트 - 유저가 null")
    void 스터디_생성_테스트__유저가_null() {
        //given DTO 가 주어졌을 때
        StudyDto.ToCreate dto = new StudyDto.ToCreate(3, "desc");

        //expect 유저를 null 로 스터디를 생성하면 IllegalArgumentException 예외가 발생한다.
        assertThatThrownBy(() -> studyService.createNew(null, dto))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("스터디 리더가 null 입니다.");
    }

    @Test
    @DisplayName("스터디 생성 테스트 - 제한 인원 양수 아님")
    void 스터디_생성_테스트__제한_인원_양수_아님() {
        //given 스터디 리더와 제한 인원이 음수인 DTO 가 주어졌을 때
        User user = mock(User.class);
        StudyDto.ToCreate dto = new StudyDto.ToCreate(-1, "desc");

        //expect 주어진 리더와 DTO 로 스터디를 생성하면 IllegalArgumentException 예외가 발생한다.
        assertThatThrownBy(() -> studyService.createNew(user, dto))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("인원 제한은 양수 값이어야 합니다.");
    }

    @Test
    @DisplayName("ID 로 단일 스터디 리소스 조회")
    void ID_로_단일_스터디_리소스_조회() {
        //given 영속된 스터디가 주어졌을 때
        User leader = spy(User.builder().nickname("nickname").build());
        when(leader.getId()).thenReturn(1L);

        long studyId = 2L;
        Study study = spy(Study.builder().leader(leader).maxParticipant(3).build());
        when(study.getId()).thenReturn(studyId);

        when(studyRepository.findById(studyId)).thenReturn(of(study));

        //when 영속 식별값으로 스터디 리소스를 조회하면
        StudyDto.Resource result = studyService.get(studyId);

        //then findById() 를 참조하고 주어진 스터디로 변환한 Resource DTO 가 반환됨
        verify(studyService).findById(studyId);
        assertThat(result).isEqualTo(StudyDto.Resource.from(study));
    }

    @Test
    @DisplayName("ID 로 단일 스터디 엔티티 조회")
    void ID_로_단일_스터디_엔티티_조회() {
        //given 영속된 스터디가 주어졌을 때
        User leader = spy(User.builder().nickname("nickname").build());
        when(leader.getId()).thenReturn(1L);

        long studyId = 2L;
        Study study = spy(Study.builder().leader(leader).maxParticipant(3).build());
        when(study.getId()).thenReturn(studyId);

        when(studyRepository.findById(studyId)).thenReturn(of(study));

        //when 영속 식별값으로 스터디 엔티티를 조회하면
        Study result = studyService.findById(studyId);

        //then 스터디가 반환됨
        assertThat(result).isEqualTo(study);
    }

    @Test
    @DisplayName("ID 로 단일 스터디 엔티티 조회 - 조회 결과 없음")
    void ID_로_단일_스터디_조회__조회_결과_없음() {
        //given 유효하지 않은 식별값이 주어졌을 때
        long studyId = 1L;
        when(studyRepository.findById(studyId)).thenReturn(empty());

        //expect 주어진 식별값으로 스터디 엔티티를 조회하면 NoSuchElementException 예외가 발생
        assertThatThrownBy(() -> studyService.findById(studyId))
                .isExactlyInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("해당 스터디를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("전체 스터디 조회")
    void 전체_스터디_조회() {
        //given 3개의 스터디가 영속되어있을 때
        User leader = spy(User.builder().nickname("nickname").build());
        when(leader.getId()).thenReturn(1L);

        Study study1 = spy(Study.builder().leader(leader).description("desc1").maxParticipant(1).build());
        when(study1.getId()).thenReturn(1L);
        Study study2 = spy(Study.builder().leader(leader).description("desc2").maxParticipant(2).build());
        when(study2.getId()).thenReturn(2L);
        Study study3 = spy(Study.builder().leader(leader).description("desc3").maxParticipant(3).build());
        when(study3.getId()).thenReturn(3L);
        List<Study> mockedList = List.of(study1, study2, study3);
        when(studyRepository.findAll()).thenReturn(mockedList);
                
        //when 전체 스터디를 조회하면
        List<StudyDto.Resource> result = studyService.getAll();

        //then 각 스터디가 Resource DTO 형태로 변환되어 반환됨
        assertThat(result).isEqualTo(
                mockedList.stream().map(StudyDto.Resource::from).collect(Collectors.toList()));
    }

    @Test
    @DisplayName("스터디 정보 수정")
    void 스터디_정보_수정() {
        //given 스터디와 리더 유저, 주어진 스터디를 업데이트하는 DTO 가 주어졌을 때
        User leader = mock(User.class);

        long studyId = 1L;
        Study study = spy(Study.builder().leader(leader).maxParticipant(3).build());
        when(study.getId()).thenReturn(studyId);
        when(studyRepository.findById(studyId)).thenReturn(of(study));

        int maxParticipant = 5;
        String description = "new desc";
        StudyDto.ToUpdate dto = spy(new StudyDto.ToUpdate(studyId, maxParticipant, description));

        //when 리더로 스터디를 업데이트하면
        studyService.updateBy(leader, dto);

        //then findById() 를 참조하고 DTO 의 업데이트 함수를 콜한다.
        verify(studyService).findById(studyId);
        verify(dto).update(study);
    }

    @Test
    @DisplayName("스터디 정보 수정 - 스터디 리더가 아닌 경우")
    void 스터디_정보_수정__스터디_리더가_아닌_경우() {
        //given 스터디와 리더가 아닌 유저, 주어진 스터디를 업데이트하는 DTO 가 주어졌을 때
        User user = mock(User.class);

        long studyId = 1L;
        Study study = spy(Study.builder().leader(mock(User.class)).maxParticipant(3).build());
        when(study.getId()).thenReturn(studyId);
        when(studyRepository.findById(studyId)).thenReturn(of(study));

        StudyDto.ToUpdate dto = spy(new StudyDto.ToUpdate(studyId, 3, "desc"));

        //expect 리더가 아닌 유저로 스터디를 업데이트하면 IllegalStateException 예외가 발생함
        assertThatThrownBy(() -> studyService.updateBy(user, dto))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("스터디 관리 권한이 없습니다.");
    }

    @Test
    @DisplayName("스터디 삭제")
    void 스터디_삭제() {
        //given 스터디와 리더가 주어졌을 때
        User leader = mock(User.class);

        long studyId = 2L;
        Study toDelete = spy(Study.builder().leader(leader).maxParticipant(3).build());
        when(toDelete.getId()).thenReturn(studyId);
        when(studyRepository.findById(studyId)).thenReturn(of(toDelete));

        //when 리더가 스터디를 제거하면
        studyService.delete(leader, studyId);

        //then findById() 를 참조하고 스터디가 제거됨
        verify(studyService).findById(studyId);
        verify(studyRepository).delete(toDelete);
    }

    @Test
    @DisplayName("스터디 삭제 - 스터디 리더가 아닌 경우")
    void 스터디_삭제__스터디_리더가_아닌_경우() {
        //given 스터디와 리더가 주어졌을 때
        User user = mock(User.class);

        long studyId = 2L;
        Study toDelete = spy(Study.builder().leader(mock(User.class)).maxParticipant(3).build());
        when(toDelete.getId()).thenReturn(studyId);
        when(studyRepository.findById(studyId)).thenReturn(of(toDelete));

        //expect 리더가 아닌 유저로 스터디를 삭제하면 IllegalStateException 예외가 발생함
        assertThatThrownBy(() -> studyService.delete(user, studyId))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("스터디 관리 권한이 없습니다.");
    }

}