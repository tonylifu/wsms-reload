package com.lifu.wsms.reload.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.lifu.wsms.reload.entity.student.Student;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AppUtil implements ApplicationContextAware {
    private static JdbcTemplate jdbcTemplate;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();
    // Regular expression pattern for the studentId format
    private static final String STUDENT_ID_PATTERN = "KSK-\\d{4}-\\d{4}";
    private static final Pattern QUOTED_STRING_PATTERN = Pattern.compile("\"([^\"]*)\"");

    // Pattern object for compiling the regular expression
    private static final Pattern STUDENT_ID_REGEX = Pattern.compile(STUDENT_ID_PATTERN);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String DATE_FORMAT_REGEX = "\\d{4}-\\d{2}-\\d{2}";
    public static final String BAD_REQUEST_CODE = "400";
    public static final String FAILED_AUTHENTICATION_CODE = "401";
    public static final String BAD_REQUEST_INVALID_PARAMS_CODE = "402";
    public static final String FAILED_AUTHORIZATION_CODE = "403";
    public static final String RESOURCE_NOT_FOUND_CODE = "404";
    public static final String INVALID_JSON_REQUEST_CODE = "407";
    public static final String PSQL_PERSISTENCE_ERROR_CODE = "408";
    //Application Specific Errors
    //Success
    public static final String TRANSACTION_OKAY_CODE = "000";
    public static final String TRANSACTION_CREATED_CODE = "001";
    public static final String TRANSACTION_SUCCESS_CODE = "003";
    public static final String TRANSACTION_UPDATED_CODE = "004";

    //Errors
    public static final String INVALID_STUDENT_ID_CODE = "900";
    public static final String MISSING_NAMES_CODE = "901";
    public static final String INVALID_DOB_CODE = "902";
    public static final String MISSING_USER_NAME_CODE = "903";
    public static final String WEAK_PASSWORD_CODE = "904";
    //Data Persistence
    public static final String DATA_PERSISTENCE_ERROR_CODE = "950";
    public static final String DUPLICATE_PERSISTENCE_ERROR_CODE = "951";
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
     * Converts a {@link LocalDateTime} object to a long value representing the number of milliseconds
     * since the epoch of 1970-01-01T00:00:00Z (UTC).
     *
     * @param localDateTime the {@link LocalDateTime} to convert
     * @return the number of milliseconds since the epoch of 1970-01-01T00:00:00Z (UTC)
     * @throws NullPointerException if {@code localDateTime} is {@code null}
     */
    public static long convertLocalDateTimeToLong(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    /**
     * Converts a long value representing the number of milliseconds since the epoch of 1970-01-01T00:00:00Z (UTC)
     * to a {@link LocalDateTime} object.
     *
     * @param milliseconds the number of milliseconds since the epoch of 1970-01-01T00:00:00Z (UTC)
     * @return the {@link LocalDateTime} corresponding to the given number of milliseconds
     */
    public static LocalDateTime convertLongToLocalDateTime(long milliseconds) {
        return Instant.ofEpochMilli(milliseconds).atZone(ZoneOffset.UTC).toLocalDateTime();
    }

    /**
     * Method to generate a studentId following the specified format
     * @param studentNumber
     * @return String
     */
    public static String generateStudentId(int currentYear, String studentNumber) {
        return "KSK-" + currentYear + "-" + studentNumber;
    }

    /**
     * Method to check if a given studentId matches the specified format
     * @param studentId
     * @return boolean
     */
    public static boolean isValidStudentId(String studentId) {
        return STUDENT_ID_REGEX.matcher(studentId).matches();
    }

    /**
     * Converts a JSON array represented by a JsonNode into a list of objects of the specified target type.
     *
     * @param jsonNode   The JSON array node to convert to a list.
     * @param targetType The class of the target type to convert each JSON object into.
     * @param <T>        The type of objects in the resulting list.
     * @return A list containing objects of the specified target type converted from the JSON array.
     */
    public static <T> List<T> convertJsonNodeToList(JsonNode jsonNode, Class<T> targetType) {
        return StreamSupport.stream(jsonNode.spliterator(), false)
                .map(node -> objectMapper.convertValue(node, targetType))
                .collect(Collectors.toList());
    }

    /**
     * Converts a list of objects into a JSON array represented by a JsonNode.
     *
     * @param list The list of objects to convert to a JSON array.
     * @return A JsonNode representing the JSON array containing the objects from the input list.
     */
    public static JsonNode convertListToJsonNode(List<?> list) {
        return objectMapper.<ArrayNode>valueToTree(list);
    }

    /**
     * Checks if a string represents a valid LocalDate in the format 'yyyy-MM-dd'.
     *
     * @param date The string to check for validity.
     * @return {@code true} if the string represents a valid LocalDate, {@code false} otherwise.
     */
    public static boolean isValidLocalDateString(String date) {
        return Pattern.matches(DATE_FORMAT_REGEX, date);
    }

    /**
     * Checks if a string is parseable as a LocalDate object using the specified date format.
     *
     * @param dateString The string to check for parseability.
     * @return {@code true} if the string is parseable as a LocalDate, {@code false} otherwise.
     */
    public static boolean isParseableLocalDateString(String dateString) {
        try {
            var date = LocalDate.parse(dateString, DATE_FORMATTER);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Parses a string representing a date into a LocalDate object using the specified date format.
     *
     * @param dateString The string representing the date to parse.
     * @return The LocalDate object parsed from the string.
     * @throws DateTimeParseException If the string cannot be parsed into a LocalDate.
     */
    public static LocalDate parseToLocalDate(String dateString) throws DateTimeParseException {
        return LocalDate.parse(dateString, DATE_FORMATTER);
    }

    /**
     * Retrieves the username of the currently authenticated user from the security context.
     * If the user is authenticated, their username is returned. If the user is not authenticated,
     * a default value is returned.
     *
     * @return The username of the authenticated user if available, otherwise a default value.
     */
    public static String getUserFromSecurityContext() {
        // Get the current authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            // Get the principal (user) from the authentication object
            Object principal = authentication.getPrincipal();

            // Check if the principal is a UserDetails object (usually represents the user)
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                // Access user details like username, authorities, etc.
                return userDetails.getUsername();
            } else if (principal instanceof String) {
                // Handle string principal (e.g., username)
                return (String) principal;
            } else if (principal instanceof OAuth2User) {
                // Handle OAuth2 user
                OAuth2User oAuth2User = (OAuth2User) principal;
                // Retrieve the username from OAuth2 user details
                return oAuth2User.getName();
            } else {
                // Handle other types of principals if needed
                return "test";
            }
        } else {
            // Handle unauthenticated users if needed
            return "unsecured testing";
        }
    }

    public static List<String> extractQuotedStrings(String message) {
        if (message == null) {
            return List.of();
        }
        int count = 20;
        List<String> extractedQuotes = new ArrayList<>();
        var matcher = QUOTED_STRING_PATTERN.matcher(message);
        while (matcher.find()) {
            if (count == 0) {
                break;
            }
            var quotedString = matcher.group(1);
            System.out.println("Quoted string: " + quotedString);
            extractedQuotes.add(quotedString);
            count--;
        }
        return extractedQuotes;
    }

    public static String getFullName(String firstName, String middleName, String lastName) {
        StringBuilder fullName = new StringBuilder(firstName);
        if (middleName != null && !middleName.isEmpty()) {
            fullName.append(" ").append(middleName);
        }
        if (lastName != null && !lastName.isEmpty()) {
            fullName.append(" ").append(lastName);
        }
        return fullName.toString();
    }
}
