package com.exadel.coolDesking.config.security;

public enum ApplicationUserPermission {
    WORKPLACE_READ("workplace:read"),
    WORKPLACE_WRITE("workplace:write");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
