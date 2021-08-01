package com.cenibee.project.studygroup.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("OAuth2Identifier VO 단위 테스트")
class OAuth2IdentifierTest {

    @Test
    @DisplayName("정적 팩터리 메서드 테스트")
    void 정적_팩터리_메서드_테스트() {
        //given OAuth2Identifier 속성이 주어졌을 때
        String provider = "google";
        String providerId = "id";

        //when 주어진 속성으로 OAuth2Identifier 정적 팩터리 메서드를 호출하면
        OAuth2Identifier result = OAuth2Identifier.of(provider, providerId);

        //then 주어진 속성이 설정된 OAuth2Identifier 가 생성됨
        assertThat(result.getProvider()).isEqualTo(Provider.valueOf(provider));
        assertThat(result.getProviderId()).isEqualTo(providerId);
    }

}