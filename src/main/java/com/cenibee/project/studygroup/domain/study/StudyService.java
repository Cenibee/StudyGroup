package com.cenibee.project.studygroup.domain.study;

import com.cenibee.project.studygroup.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class StudyService {

    private final StudyRepository studyRepository;

    public Long createNew(User leader, StudyDto.ToCreate dto) {
        Study newStudy = dto.toEntityWith(leader);
        return studyRepository.save(newStudy).getId();
    }
}
