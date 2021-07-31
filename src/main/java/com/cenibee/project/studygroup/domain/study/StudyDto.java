package com.cenibee.project.studygroup.domain.study;

import com.cenibee.project.studygroup.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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

    @AllArgsConstructor
    @EqualsAndHashCode
    @Getter
    class Resource {
        private final long studyId;
        private final long leaderId;
        private final String leaderNickname;
        private final int numOfParticipant;
        private final int maxParticipant;
        private final String description;

        public static Resource from(Study entity) {
            User leader = entity.getLeader();
            return new Resource(entity.getId(),
                    leader.getId(),
                    leader.getNickname(),
                    entity.getNumOfParticipants(),
                    entity.getMaxParticipant(),
                    entity.getDescription());
        }

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    class ToUpdate {
        private long studyId;
        private int maxParticipant;
        private String description;

        public void update(Study study) {
            if (this.studyId == study.getId()) {
                study.update(this.maxParticipant, this.description);
            } else {
                throw new IllegalArgumentException("id 불일치로 스터디 정보를 업데이트 할 수 없습니다.");
            }
        }
    }
}
