package com.lifu.wsms.reload.api;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.regex.Pattern;

public class AppUtil {
    // Regular expression pattern for the studentId format
    private static final String STUDENT_ID_PATTERN = "KSK/\\d{4}/\\d{4}";

    // Pattern object for compiling the regular expression
    private static final Pattern STUDENT_ID_REGEX = Pattern.compile(STUDENT_ID_PATTERN);
    public static final String BAD_REQUEST_CODE = "400";
    public static final String FAILED_AUTHENTICATION_CODE = "401";
    public static final String BAD_REQUEST_INVALID_PARAMS_CODE = "402";
    public static final String FAILED_AUTHORIZATION_CODE = "403";
    public static final String RESOURCE_NOT_FOUND_CODE = "404";
    //Application Specific Errors
    //Success
    public static final String TRANSACTION_OKAY_CODE = "000";
    public static final String TRANSACTION_CREATED_CODE = "001";
    public static final String TRANSACTION_SUCCESS_CODE = "003";

    //Errors
    public static final String INVALID_STUDENT_ID_CODE = "900";
    public static final String MISSING_NAMES_CODE = "901";
    public static final String INVALID_DOB_CODE = "902";
    //Data Persistence
    public static final String DATA_PERSISTENCE_ERROR_CODE = "950";
    public static final String UNKNOWN_ERROR_CODE = "999";

    private AppUtil(){}

    /**
     * This method concerts local date to long
     * @param localDate
     * @return long
     */
    public static long convertLocalDateToLong(LocalDate localDate) {
        return localDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    /**
     * This method converts long date to local date
     * @param date
     * @return LocalDate
     */
    public static LocalDate convertLongToLocalDate(long date) {
        return Instant.ofEpochMilli(date).atZone(ZoneOffset.UTC).toLocalDate();
    }

    /**
     * Method to generate a random four-digit number
     * @return int
     */
    private static int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(9000) + 1000; // Generates a random number between 100 and 999
    }

    /**
     * Method to generate a studentId following the specified format
     * @return String
     */
    public static String generateStudentId() {
        int currentYear = Year.now().getValue();
        int randomNumber = generateRandomNumber();
        return "KSK/" + currentYear + "/" + randomNumber;
    }

    /**
     * Method to check if a given studentId matches the specified format
     * @param studentId
     * @return boolean
     */
    public static boolean isValidStudentId(String studentId) {
        return STUDENT_ID_REGEX.matcher(studentId).matches();
    }
}
