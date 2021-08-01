package com.cenibee.project.studygroup.domain.study;

import com.cenibee.project.studygroup.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/study")
public class StudyController {

    private final StudyService studyService;

    @PostMapping
    public ResponseEntity<?> create(User user, StudyDto.ToCreate dto) {
        Long newStudyId = studyService.createNew(user, dto);
        return ResponseEntity.created(getLocation(newStudyId)).build();
    }

    @GetMapping("{studyId}")
    public ResponseEntity<?> get(@PathVariable long studyId) {
        return ResponseEntity.ok(studyService.get(studyId));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(studyService.getAll());
    }

    @PatchMapping
    public ResponseEntity<?> patch(User user, StudyDto.ToUpdate dto) {
        studyService.updateBy(user, dto);
        return ResponseEntity.noContent()
                .header(HttpHeaders.LOCATION, getLocation(dto.getStudyId()).toString())
                .build();
    }

    @DeleteMapping("{studyId}")
    public ResponseEntity<?> delete(User user, @PathVariable long studyId) {
        studyService.delete(user, studyId);
        return ResponseEntity.noContent().build();
    }

    private URI getLocation(Long newStudyId) {
        return URI.create("/api/study/" + newStudyId);
    }
}
