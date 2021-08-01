package com.cenibee.project.studygroup.domain.user;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@EqualsAndHashCode
@Embeddable
public class OAuth2Identifier {

    @Enumerated(EnumType.STRING)
    private Provider provider;
    private String providerId;

    public static OAuth2Identifier of(String provider, String providerId) {
        return new OAuth2Identifier(Provider.valueOf(provider), providerId);
    }

    @Builder
    protected OAuth2Identifier(Provider provider, String providerId) {
        this.provider = provider;
        this.providerId = providerId;
    }

}
