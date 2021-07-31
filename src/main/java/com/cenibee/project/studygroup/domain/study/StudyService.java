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
        return StudyDto.Resource.from(findById(studyId));
    }

    @Transactional(readOnly = true)
    public Study findById(long studyId) {
        return studyRepository.findById(studyId).orElseThrow(() ->
                new NoSuchElementException("해당 스터디를 찾을 수 없습니다.(id:" + studyId + ")"));
    }

    @Transactional(readOnly = true)
    public List<StudyDto.Resource> getAll() {
        return studyRepository.findAll().stream()
                .map(StudyDto.Resource::from)
                .collect(Collectors.toList());
    }

    public void updateBy(User leader, StudyDto.ToUpdate dto) {
        Study study = findById(dto.getStudyId());
        checkLeader(leader, study);
        dto.update(study);
    }

    public void delete(User leader, long studyId) {
        Study study = findById(studyId);
        checkLeader(leader, study);
        studyRepository.delete(study);
    }

    private void checkLeader(User leader, Study study) {
        if (leader == null || !leader.equals(study.getLeader())) {
            throw new IllegalStateException("스터디 관리 권한이 없습니다.");
        }
    }
}
