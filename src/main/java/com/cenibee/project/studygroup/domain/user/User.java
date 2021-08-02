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
import static java.util.Optional.ofNullable;
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

    // TODO 유효성 검증
    @Builder
    public User(OAuth2Identifier identifier, String nickname, String email, Role role) {
        this.identifier = identifier;
        this.nickname = nickname;
        this.email = email;
        this.role = role != null ? role : USER;
    }

    //===비즈니스 로직===//

    /**
     * TODO 유효성 검증
     * 변경 가능한 정보를 업데이트 합니다. null 로 입력된 속성은 변경되지 않습니다.
     * @param newNickname 변경할 닉네임
     * @param newEmail 변경할 이메일
     */
    public void update(String newNickname, String newEmail) {
        ofNullable(newNickname).ifPresent(nickname -> this.nickname = nickname);
        ofNullable(newEmail).ifPresent(email -> this.email = email);
    }
}
