package com.lifu.wsms.reload.service.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {

    @Test
    void isPasswordStrong() {
        char[] strongPassword = "Password@123".toCharArray();
        char[] weakPassword = "password".toCharArray();
        assertTrue(PasswordValidator.isPasswordStrong(strongPassword));
        assertFalse(PasswordValidator.isPasswordStrong(weakPassword));
    }
}