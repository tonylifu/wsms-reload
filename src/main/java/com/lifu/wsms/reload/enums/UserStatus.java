package com.lifu.wsms.reload.enums;

public enum UserStatus {
    CREATED("Created"),
    ACTIVE("Active"),
    DEACTIVATED("Deactivated"),
    RESTRICTED("Restricted"),
    OTHER("Other");

    private final String displayName;

    UserStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static UserStatus fromString(String value) {
        for (UserStatus studentStatus : UserStatus.values()) {
            if (studentStatus.displayName.equalsIgnoreCase(value)) {
                return studentStatus;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }
}
