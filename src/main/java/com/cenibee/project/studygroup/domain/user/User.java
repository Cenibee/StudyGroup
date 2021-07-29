package com.cenibee.project.studygroup.domain.user;

import com.cenibee.project.studygroup.domain.base.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Entity
public class User extends BaseEntity {

    private String nickname;
    private String email;

    @Builder
    public User(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }
}
