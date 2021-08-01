package com.cenibee.project.studygroup.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired TestEntityManager em;
    @Autowired UserRepository userRepository;

    @Test
    @DisplayName("OAuth2 식별자로 유저 조회")
    void OAuth2_식별자로_유저_조회() {
        //given OAuth2Identifier 와 주어진 OAuth2Identifier 가 설정된 영속된 유저가 주어졌을 때
        OAuth2Identifier identifier = OAuth2Identifier.of("google", "id");
        User user = em.persist(User.builder().identifier(identifier).build());

        //when 주어진 OAuth2Identifier 로 유저를 조회하면
        User result = userRepository.findByIdentifier(identifier).orElseThrow(RuntimeException::new);

        //then 주어진 유저가 조회됨
        assertThat(result).isEqualTo(user);
    }

    @Test
    @DisplayName("OAuth2 식별자로 유저 조회 - 없는 속성")
    void OAuth2_식별자로_유저_조회__없는_속성() {
        //given 영속된 유저가 없는 OAuth2Identifier 가 주어졌을 때
        OAuth2Identifier identifier = OAuth2Identifier.of("google", "id");

        //when 주어진 OAuth2Identifier 로 유저를 조회하면
        Optional<User> result = userRepository.findByIdentifier(identifier);

        //then 빈 Optional 이 반환됨
        assertThat(result.isEmpty()).isTrue();
    }

}