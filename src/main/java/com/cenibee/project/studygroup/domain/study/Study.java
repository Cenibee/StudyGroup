package com.cenibee.project.studygroup.domain.study;

import com.cenibee.project.studygroup.domain.base.BaseEntity;
import com.cenibee.project.studygroup.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Entity
public class Study extends BaseEntity {

    @OneToOne(fetch = LAZY, optional = false)
    private User leader;

    @OneToMany(fetch = LAZY, mappedBy = "study", cascade = PERSIST, orphanRemoval = true)
    private List<Participate> participants = new ArrayList<>();

    @Column(nullable = false)
    private int maxParticipant;

    private String description;

    @Builder
    public Study(User leader, int maxParticipant, String description) {
        if (leader == null) {
            throw new IllegalArgumentException("스터디 리더가 null 입니다.");
        } else if (maxParticipant <= 0) {
            throw new IllegalArgumentException("인원 제한은 양수 값이어야 합니다.");
        }

        this.leader = leader;
        this.participants.add(Participate.of(leader, this));
        this.maxParticipant = maxParticipant;
        this.description = description;
    }

    //===비즈니스 로직===//

    /**
     * 스터디에 참가한 인원 수를 반환합니다.
     * @return 스터디에 참가한 인원 수
     */
    public int getNumOfParticipants() {
        return participants.size();
    }

    /**
     * user 의 스터디 참가 여부를 반환합니다.
     * @param user 스터디 참가 여부를 확인할 {@link User} 객체
     * @return {@code true} - 참가되어 있음, {@code false} - 참가되어 있지 않음
     */
    public boolean existsUser(User user) {
        if (user == null) return false;
        if (leader.equals(user)) return true;
        return participants.stream()
                .anyMatch(p -> p.getParticipant().equals(user));
    }

    /**
     * 변경 가능한 정보를 업데이트 합니다. null 로 입력된 속성은 변경되지 않습니다.<p/>
     * newMaxParticipant 속성은 양수만 허용하며, 음수거나 현재 속한 인원보다 작은 값인 경우
     * {@link IllegalArgumentException} 예외가 발생합니다.
     * @param newMaxParticipant 변경할 스터디원 제한 인원
     * @param newDescription 변경할 스터디 설명
     * @exception IllegalArgumentException 유효하지 않은 속성이 포함되어 있는 경우 발생하며,
     * 해당 예외가 발생한 경우 모든 속성이 업데이트되지 않습니다.
     */
    public void update(Integer newMaxParticipant, String newDescription) {
        if (newMaxParticipant != null && newMaxParticipant <= 0) {
            throw new IllegalArgumentException("인원 제한은 양수 값이어야 합니다.");
        } else if (newMaxParticipant != null && newMaxParticipant < getNumOfParticipants()) {
            throw new IllegalArgumentException("현재 스터디원 보다 적은 인원 제한은 설정할 수 없습니다.");
        }
        ofNullable(newMaxParticipant).ifPresent(maxParticipant ->
                this.maxParticipant = maxParticipant);
        ofNullable(newDescription).ifPresent(description ->
                this.description = description);
    }

    /**
     * user 를 스터디에 참가시키고, user 의 참가 정보를 반환합니다.<p/>
     * user 가 이미 참가해있는 경우, user 의 참가 정보를 찾아서 반환합니다.(멱등성)
     * @param user 스터디에 참가시킬 유저
     * @return 주어진 유저의 스터디 참가 정보 엔티티
     * @exception IllegalStateException 주어진 user 가 null 인 경우
     * @exception IllegalArgumentException 스터디 제한 인원을 초과하는 경우
     */
    public Participate join(User user) {
        if (user == null) {
            throw new IllegalArgumentException("유저가 null 입니다.");
        }
        if (participants.size() >= maxParticipant) {
            throw new IllegalStateException("더 이상 참가할 수 없습니다.");
        }
        return participants.stream()
                .filter(p -> p.getParticipant().equals(user))
                .findFirst()
                .orElseGet(() -> {
                    Participate newParticipate = Participate.of(user, this);
                    this.participants.add(newParticipate);
                    return newParticipate;
                });
    }

    /**
     * user 를 스터디에서 탈퇴시킵니다. 탈퇴 성공 여부가 반환됩니다.<p/>
     * 단, 스터디 리더는 탈퇴 요청시 항상 실패합니다.
     * @param user 스터디에서 탈퇴시킬 유저
     * @return 탈퇴 성공 여부
     */
    public boolean remove(User user) {
        if (user == null || leader.equals(user)) return false;
        return participants.removeIf(p -> p.getParticipant().equals(user));
    }

}
