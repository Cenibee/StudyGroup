package com.cenibee.project.studygroup.domain.user;

public enum Role {
    ADMIN, USER;

    public String getRoleKey() {
        return "ROLE_" + this.name();
    }
}
