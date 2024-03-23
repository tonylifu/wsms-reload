package com.lifu.wsms.reload.enums;

public enum StudentStatus {
    CREATED("Created"),
    ACTIVE("Active"),
    SUSPENDED("Suspended"),
    RUSTICATED("Rusticated"),
    TRANSFERRED("Transferred"),
    WITHDRAWN("Withdrawn"),
    GRADUATED("Graduated"),
    OTHER("Other");

    private final String displayName;

    StudentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static StudentStatus fromString(String value) {
        for (StudentStatus studentStatus : StudentStatus.values()) {
            if (studentStatus.displayName.equalsIgnoreCase(value)) {
                return studentStatus;
            }
        }
        throw new IllegalArgumentException("Unknown gender: " + value);
    }
}
