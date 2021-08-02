package com.cenibee.project.studygroup.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface UserDto {

    @AllArgsConstructor
    @Getter
    class ToUpdate {
        private final long userId;
        private final String email;
        private final String nickname;

        public void update(User user) {
            if (user.getId() == this.userId) {
                user.update(nickname, email);
            } else {
                throw new IllegalArgumentException("id 불일치로 유저 정보를 업데이트 할 수 없습니다.");
            }
        }
    }

}
