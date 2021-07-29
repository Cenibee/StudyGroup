package com.cenibee.project.studygroup.domain.study;

import com.cenibee.project.studygroup.domain.base.BaseEntity;
import com.cenibee.project.studygroup.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Entity
public class Study extends BaseEntity {

    @Column(nullable = false)
    @OneToOne(fetch = LAZY)
    private User leader;

    @OneToMany(fetch = LAZY, mappedBy = "study")
    private List<Participate> participants = new ArrayList<>();

    @Column(nullable = false)
    private int maxParticipant;

    private String description;

    @Builder
    public Study(User leader, int maxParticipant, String description) {
        this.leader = leader;
        this.participants.add(Participate.of(leader, this));
        this.maxParticipant = maxParticipant;
        this.description = description;
    }
}
