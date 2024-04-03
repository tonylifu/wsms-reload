package com.lifu.wsms.reload.enums;

public enum UserRolesGroup {
    ADMIN("ADMIN"),
    STANDARD("STANDARD"),
    CASHIER("CASHIER"),
    SECURITY("SECURITY"),
    NONE("NONE");

    private final String displayName;

    UserRolesGroup(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static UserRolesGroup fromString(String value) {
        for (UserRolesGroup userRole : UserRolesGroup.values()) {
            if (userRole.displayName.equalsIgnoreCase(value)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + value);
    }
}
