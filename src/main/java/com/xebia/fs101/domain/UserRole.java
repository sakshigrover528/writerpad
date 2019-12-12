package com.xebia.fs101.domain;

public enum UserRole {
    WRITER, EDITOR;

    public String getRoleName() {
        return "ROLE_" + this.name();
    }
}
