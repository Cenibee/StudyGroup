package com.cenibee.project.studygroup.domain.study;

import com.cenibee.project.studygroup.BaseMockTest;
import com.cenibee.project.studygroup.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
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

}