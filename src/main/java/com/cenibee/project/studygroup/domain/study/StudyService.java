package com.cenibee.project.studygroup.domain.study;

import com.cenibee.project.studygroup.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class StudyService {

    private final StudyRepository studyRepository;

    public Long createNew(User leader, StudyDto.ToCreate dto) {
        Study newStudy = dto.toEntityWith(leader);
        return studyRepository.save(newStudy).getId();
    }

    @Transactional(readOnly = true)
    public StudyDto.Resource get(long studyId) {
        return StudyDto.Resource.from(studyRepository.findById(studyId).orElseThrow(() ->
                new NoSuchElementException("해당 스터디를 찾을 수 없습니다.(id:" + studyId + ")")));
    }

    @Transactional(readOnly = true)
    public List<StudyDto.Resource> getAll() {
        return studyRepository.findAll().stream()
                .map(StudyDto.Resource::from)
                .collect(Collectors.toList());
    }

}
