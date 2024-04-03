package com.lifu.wsms.reload.enums;

public enum UserPermission {
    //Admin
    ADMIN("ADMIN"),

    //Student
    STUDENT_CREATE("STUDENT_CREATE"),
    STUDENT_READ("STUDENT_READ"),
    STUDENT_UPDATE("STUDENT_UPDATE"),
    STUDENT_DELETE("STUDENT_DELETE"),

    //Account
    ACCOUNT_CREATE("ACCOUNT_CREATE"),
    ACCOUNT_READ("ACCOUNT_READ"),
    ACCOUNT_UPDATE("ACCOUNT_UPDATE"),
    ACCOUNT_DELETE("ACCOUNT_DELETE"),
    NONE("NONE");

    private final String displayName;

    UserPermission(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static UserPermission fromString(String value) {
        for (UserPermission userRole : UserPermission.values()) {
            if (userRole.displayName.equalsIgnoreCase(value)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + value);
    }
}
