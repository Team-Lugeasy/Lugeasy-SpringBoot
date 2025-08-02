package com.jimjim.lugeasy.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PermissionRole {
    ROLE_ADMIN("ADMIN"),
    ROLE_CLIENT("CLIENT"),
    ROLE_DEVELOPER("DEVELOPER");
    private final String name;
}
