package com.cenibee.project.studygroup.domain.study;

import com.cenibee.project.studygroup.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("StudyRepository JPA 슬라이스 테스트")
class StudyRepositoryJpaTest {

    @Autowired TestEntityManager em;
    @Autowired StudyRepository repository;

    @Test
    @DisplayName("스터디 생성 테스트 - 참가 정보 CASCADE")
    void 스터디_생성_테스트__참가_정보_CASCADE() {
        //given 저장된 유저를 리더로하는 비영속 스터디가 주어졌을 때
        User user = em.persist(User.builder().build());

        Study study = Study.builder().leader(user).maxParticipant(3).build();

        //when 스터디를 저장하면
        Study result = repository.save(study);

        //then 스터디와 리더 참가 정보가 영속되어 ID 가 생성된다.
        assertThat(result.getId()).isNotNull();
        assertThat(result.getParticipants().get(0).getId()).isNotNull();
    }

    @Test
    @DisplayName("스터디원 참가 테스트 - 참가 정보 CASCADE")
    void 스터디원_추가_테스트__참가_정보_CASCADE() {
        //given 스터디와 스터디에 참가하지 않은 유저가 주어졌을 때
        Study study = em.persist(Study.builder()
                .leader(em.persist(User.builder().build()))
                .maxParticipant(3).build());
        User newParticipant = em.persist(User.builder().build());

        //when 유저가 스터디에 참가하고 flush 가 발생하면
        Participate newParticipate = study.join(newParticipant);
        em.flush();

        //then 새로 참가한 유저 참가 정보가 영속되어 ID 가 생성된다.
        assertThat(newParticipate.getId()).isNotNull();
    }

    @Test
    @DisplayName("스터디원 탈퇴 테스트 - 참가 정보 orphanRemoval")
    void 스터디원_탈퇴_테스트__참가_정보_orphanRemoval() {
        //given
        Study study = em.persist(Study.builder()
                .leader(em.persist(User.builder().build()))
                .maxParticipant(3).build());
        User toRemove = em.persist(User.builder().build());
        study.join(toRemove);
        em.flush();

        //when
        study.remove(toRemove);
        em.flush();
        em.clear();

        //then
        Study find = repository.findById(study.getId()).orElseThrow();
        assertThat(find.existsUser(toRemove)).isFalse();
    }

    @Test
    @DisplayName("스터디 삭제 테스트 - 참가 정보 CASCADE (orphanRemoval)")
    void 스터디원_삭제_테스트__참가_정보_CASCADE() {
        //given 스터디원이 존재하는 스터디가 주어졌을 때
        Study study = em.persist(Study.builder()
                .leader(em.persist(User.builder().build()))
                .maxParticipant(3).build());
        User user = em.persist(User.builder().build());
        Participate joined = study.join(user);
        em.flush();

        //when 스터디를 삭제하고 flush 하면
        repository.deleteById(study.getId());
        em.flush();
        em.clear();

        //then 스터디가 삭제되고, 스터디 참가 정보도 삭제되어 존재하지 않는다.
        assertThat(repository.findById(study.getId()).isEmpty()).isTrue();
        assertThat(em.find(Participate.class, joined.getId())).isNull();
    }
}