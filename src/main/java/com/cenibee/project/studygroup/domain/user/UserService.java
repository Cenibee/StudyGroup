package com.cenibee.project.studygroup.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("해당 유저를 찾을 수 없습니다.(id:" + id + ")"));
    }

    public User createOrGet(OAuth2Identifier identifier) {
        return userRepository.findByIdentifier(identifier).orElseGet(() ->
                userRepository.save(User.builder().identifier(identifier).build()));
    }

    public void update(long loginId, UserDto.ToUpdate dto) {
        if (loginId != dto.getUserId()) {
            throw new IllegalStateException("유저 관리 권한이 없습니다.");
        }
        dto.update(findById(loginId));
    }

}
