package com.cenibee.project.studygroup.domain.study;

import com.cenibee.project.studygroup.domain.base.BaseEntity;
import com.cenibee.project.studygroup.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Entity
public class Participate extends BaseEntity {

    @ManyToOne(fetch = LAZY, optional = false)
    private User participant;

    @ManyToOne(fetch = LAZY, optional = false)
    private Study study;

    @Builder
    public Participate(User participant, Study study) {
        this.participant = participant;
        this.study = study;
    }

    public static Participate of(User participant, Study study) {
        return new Participate(participant, study);
    }

}
