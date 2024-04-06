package com.lifu.wsms.reload.service.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for password validation.
 */
public class PasswordValidator {

    /**
     * Checks if the provided password meets the criteria for a strong password.
     *
     * @param password the password to be validated as a char array.
     * @return {@code true} if the password meets the criteria for a strong password, {@code false} otherwise.
     */
    public static boolean isPasswordStrong(char[] password) {
        // Minimum length of 8 characters
        if (password.length < 8) {
            return false;
        }

        // Convert char[] to String
        String passwordString = new String(password);

        // At least one uppercase letter
        Pattern uppercasePattern = Pattern.compile("[A-Z]");
        Matcher uppercaseMatcher = uppercasePattern.matcher(passwordString);
        if (!uppercaseMatcher.find()) {
            return false;
        }

        // At least one lowercase letter
        Pattern lowercasePattern = Pattern.compile("[a-z]");
        Matcher lowercaseMatcher = lowercasePattern.matcher(passwordString);
        if (!lowercaseMatcher.find()) {
            return false;
        }

        // At least one digit
        Pattern digitPattern = Pattern.compile("[0-9]");
        Matcher digitMatcher = digitPattern.matcher(passwordString);
        if (!digitMatcher.find()) {
            return false;
        }

        // At least one special character
        Pattern specialCharPattern = Pattern.compile("[^A-Za-z0-9]");
        Matcher specialCharMatcher = specialCharPattern.matcher(passwordString);
        if (!specialCharMatcher.find()) {
            return false;
        }

        return true;
    }
}
