package com.cenibee.project.studygroup.domain.study;

import com.cenibee.project.studygroup.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface StudyDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    class ToCreate {
        private int maxParticipant;
        private String description;

        public Study toEntityWith(User leader) {
            return Study.builder()
                    .leader(leader)
                    .maxParticipant(this.maxParticipant)
                    .description(this.description)
                    .build();
        }
    }

}
