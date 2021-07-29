package com.cenibee.project.studygroup.domain.base;

import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@MappedSuperclass
public abstract class BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        if (getId() == null) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                '}';
    }
}
