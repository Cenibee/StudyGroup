package com.cenibee.project.studygroup.domain.user;

import com.cenibee.project.studygroup.domain.base.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import static com.cenibee.project.studygroup.domain.user.Role.USER;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Entity
public class User extends BaseEntity {

    private String nickname;
    private String email;

    @Embedded
    private OAuth2Identifier identifier;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(OAuth2Identifier identifier, String nickname, String email, Role role) {
        this.identifier = identifier;
        this.nickname = nickname;
        this.email = email;
        this.role = role != null ? role : USER;
    }
}
