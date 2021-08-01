package com.cenibee.project.studygroup.domain.study;

import com.cenibee.project.studygroup.BaseMockTest;
import com.cenibee.project.studygroup.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.*;

@DisplayName("StudyController 유닛 테스트")
class StudyControllerTest extends BaseMockTest {

    @InjectMocks StudyController studyController;

    @Mock StudyService studyService;

    @Test
    @DisplayName("스터디 생성 요청")
    void 스터디_생성_요청() {
        //given 유저, DTO 와 스터디 생성 서비스 결과가 주어졌을 때
        User user = mock(User.class);
        StudyDto.ToCreate dto = mock(StudyDto.ToCreate.class);

        long studyId = 1L;
        when(studyService.createNew(user, dto)).thenReturn(studyId);

        //when 주어진 유저, DTO 로 스터디 생성을 요청하면
        ResponseEntity<?> result = studyController.create(user, dto);

        //then status 201 응답 / Location 헤더에 주어진 id 의 스터디 리소스 경로가 포함됨
        assertThat(result.getStatusCode()).isEqualTo(CREATED);
        assertThat(result.getHeaders())
                .anySatisfy((key, val) -> {
                    assertThat(key).isEqualTo(LOCATION);
                    assertThat(val).contains("/api/study/" + studyId);
                });
    }

    @Test
    @DisplayName("스터디 생성 요청 - 예외 발생")
    void 스터디_생성_요청__예외_발생() {
        //given 스터디 생성 서비스에서 예외가 발생하면
        User user = mock(User.class);
        StudyDto.ToCreate dto = mock(StudyDto.ToCreate.class);

        RuntimeException ex = mock(RuntimeException.class);
        when(studyService.createNew(user, dto)).thenThrow(ex);

        //expect 해당 예외가 그대로 전이된다.
        assertThatThrownBy(() -> studyController.create(user, dto)).isEqualTo(ex);
    }

    @Test
    @DisplayName("ID 로 단일 스터디 조회 요청")
    void ID_로_단일_스터디_조회_요청() {
        //given 단일 스터디 리소스 조회 서비스 결과가 주어졌을 때
        int studyId = 1;
        StudyDto.Resource mockedDto = mock(StudyDto.Resource.class);
        when(studyService.get(studyId)).thenReturn(mockedDto);

        //when 단일 스터디 조회를 요청하면
        ResponseEntity<?> result = studyController.get(studyId);

        //then status 200 반환 / 바디에 주어진 리소스가 설정됨
        assertThat(result.getStatusCode()).isEqualTo(OK);
        assertThat(result.getBody()).isEqualTo(mockedDto);
    }

    @Test
    @DisplayName("ID 로 단일 스터디 조회 요청 - 예외 발생")
    void ID_로_단일_스터디_조회_요청__예외_발생() {
        //given 단일 스터디 리소스 조회 서비스에서 예외가 발생하면
        int studyId = 1;
        RuntimeException ex = mock(RuntimeException.class);
        when(studyService.get(studyId)).thenThrow(ex);

        //expect 해당 예외가 그대로 전이된다.
        assertThatThrownBy(() -> studyController.get(studyId))
                .isEqualTo(ex);
    }

    @Test
    @DisplayName("전체 스터디 조회 요청")
    void 전체_스터디_조회_요청() {
        //given 전체 스터디 리소스 조회 서비스 결과가 주어졌을 때
        List<StudyDto.Resource> mockedList = List.of(
                mock(StudyDto.Resource.class), mock(StudyDto.Resource.class),
                mock(StudyDto.Resource.class), mock(StudyDto.Resource.class)
        );
        when(studyService.getAll()).thenReturn(mockedList);

        //when 전체 스터디 조회 요청하면
        ResponseEntity<?> result = studyController.getAll();

        //then status 200 반환 / 바디에 주어진 리소스가 설정됨
        assertThat(result.getStatusCode()).isEqualTo(OK);
        assertThat(result.getBody()).isEqualTo(mockedList);
    }

    @Test
    @DisplayName("스터디 정보 업데이트 요청")
    void 스터디_정보_업데이트_요청() {
        //given 스터디 리더와 업데이트 DTO 가 주어졌을 때
        long studyId = 1L;
        User user = mock(User.class);
        StudyDto.ToUpdate dto = mock(StudyDto.ToUpdate.class);
        when(dto.getStudyId()).thenReturn(studyId);

        //when 리더와 DTO 로 업데이트 요청하면
        ResponseEntity<?> result = studyController.patch(user, dto);

        //then status 204 반환 / Location 헤더에 리소스 경로 설정됨 / studyService.updateBy() 가 호출됨
        assertThat(result.getStatusCode()).isEqualTo(NO_CONTENT);
        assertThat(result.getHeaders())
                .anySatisfy((key, val) -> {
                    assertThat(key).isEqualTo(LOCATION);
                    assertThat(val).contains("/api/study/" + studyId);
                });
        verify(studyService).updateBy(user, dto);
    }

    @Test
    @DisplayName("스터디 정보 업데이트 요청 - 예외 발생")
    void 스터디_정보_업데이트_요청__예외_발생() {
        //given 스터디 정보 업데이트 서비스에서 예외가 발생하면
        User user = mock(User.class);
        StudyDto.ToUpdate dto = mock(StudyDto.ToUpdate.class);

        RuntimeException ex = mock(RuntimeException.class);
        doThrow(ex).when(studyService).updateBy(user, dto);

        //expect 해당 예외가 그대로 전이된다.
        assertThatThrownBy(() -> studyController.patch(user, dto))
                .isEqualTo(ex);
    }

    @Test
    @DisplayName("스터디 삭제 요청")
    void 스터디_삭제_요청() {
        //given 스터디 ID 와 유저가 주어졌을 때
        long studyId = 1L;
        User user = mock(User.class);

        //when 주어진 값으로 스터디 삭제 요청하면
        ResponseEntity<?> result = studyController.delete(user, studyId);

        //then 주어진 값으로 스터디 삭제 서비스가 호출됨 / status 204 반환됨
        verify(studyService).delete(user, studyId);
        assertThat(result.getStatusCode()).isEqualTo(NO_CONTENT);
    }

    @Test
    @DisplayName("스터디 삭제 요청 - 예외 발생")
    void 스터디_삭제_요청__예외_발생() {
        //given 스터디 삭제 서비스에서 예외가 발생하면
        long studyId = 1L;
        User user = mock(User.class);

        RuntimeException ex = mock(RuntimeException.class);
        doThrow(ex).when(studyService).delete(user, studyId);

        //expect 해당 예외가 그대로 전이된다.
        assertThatThrownBy(() -> studyController.delete(user, studyId))
                .isEqualTo(ex);
    }

}