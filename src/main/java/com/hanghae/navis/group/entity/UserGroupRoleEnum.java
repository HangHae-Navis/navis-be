package com.hanghae.navis.group.entity;


public enum UserGroupRoleEnum {
    USER(Authority.USER),  // 사용자 권한
    SUPPORT(Authority.SUPPORT),  // 서포트 권한
    ADMIN(Authority.ADMIN);  // 관리자 권한

    private final String authority;

    UserGroupRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String SUPPORT = "ROLE_SUPPORT";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}
