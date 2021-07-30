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

}
