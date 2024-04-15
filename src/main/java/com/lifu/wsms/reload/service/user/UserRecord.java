package com.lifu.wsms.reload.service.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifu.wsms.reload.api.AppUtil;
import com.lifu.wsms.reload.api.contract.user.UserService;
import com.lifu.wsms.reload.dto.Item;
import com.lifu.wsms.reload.dto.request.user.CreateUserRequest;
import com.lifu.wsms.reload.dto.request.user.UpdateUserRequest;
import com.lifu.wsms.reload.dto.response.ApiResponse;
import com.lifu.wsms.reload.dto.response.FailureResponse;
import com.lifu.wsms.reload.dto.response.SuccessResponse;
import com.lifu.wsms.reload.entity.user.Role;
import com.lifu.wsms.reload.entity.user.User;
import com.lifu.wsms.reload.enums.UserRole;
import com.lifu.wsms.reload.enums.UserStatus;
import com.lifu.wsms.reload.mapper.user.CreateUserRequestToUserMapper;
import com.lifu.wsms.reload.mapper.user.UserToUserResponseMapper;
import com.lifu.wsms.reload.repository.RoleRepository;
import com.lifu.wsms.reload.repository.UserRepository;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lifu.wsms.reload.api.AppUtil.*;
import static com.lifu.wsms.reload.service.ApiService.*;
import static com.lifu.wsms.reload.service.user.UserApiService.validateCreateUser;

@RequiredArgsConstructor
@Slf4j
public class UserRecord implements UserService {
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public Either<FailureResponse, SuccessResponse> createUser(CreateUserRequest createUserRequest) {
        return validateCreateUser(createUserRequest)
                .fold(Either::left, validatedRequest -> createUserEntity(createUserRequest));
    }

    @Override
    public Either<FailureResponse, SuccessResponse> findUser(String username) {
        if (username == null || username.isEmpty()) {
            log.error("error, missing username: {}", username);
            return buildErrorResponse(HttpStatus.BAD_REQUEST, MISSING_USER_NAME_CODE);
        }
        username = username.strip().toUpperCase();
        try {
            if (!userRepository.existsByUsername(username)) {
                log.error("request error, username: {} does not exist", username);
                return buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE);
            }
            return Either.right(
                    userRepository.findByUsername(username)
                            .map(user -> {
                                log.info("\n\n\n{}\n\n\n", user);
                                return buildSuccessResponse(
                                        objectMapper.valueToTree(UserToUserResponseMapper.INSTANCE.toUserResponse(user)
                                        ),
                                        HttpStatus.OK, TRANSACTION_OKAY_CODE).get();
                            })
                            .orElseThrow(() -> new RuntimeException("request failed"))
            );
        } catch (DataAccessException e) {
            log.error("find request error => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE);
        } catch (Exception e) {
            log.error("an unknown error occurred => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, UNKNOWN_ERROR_CODE);
        }
    }

    @Override
    @Transactional
    public Either<FailureResponse, SuccessResponse> updateUser(UpdateUserRequest updateUserRequest) {
        try {
            if (updateUserRequest.getUsername() != null) {
                updateUserRequest.setUsername(updateUserRequest.getUsername().strip().toUpperCase());
            }
            return UserApiService.validateUpdateUser(updateUserRequest)
                    .map(result -> {
                        return userRepository.findByUsername(updateUserRequest.getUsername())
                                .map(user -> userRepository.save(UserApiService.populateUserForUpdate(user, updateUserRequest)))
                                .orElseThrow(() -> new RuntimeException("user record update failed"));
                    })
                    .map(user -> buildSuccessResponse(objectMapper.valueToTree(UserToUserResponseMapper.INSTANCE.toUserResponse(user)),
                            HttpStatus.OK, TRANSACTION_UPDATED_CODE).get()
                    );
        } catch (DataAccessException e) {
            log.error("update error => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.NOT_FOUND, DATA_PERSISTENCE_ERROR_CODE);
        } catch (RuntimeException e) {
            log.error("resource not found error => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE);
        } catch (Exception e) {
            log.error("server error => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, UNKNOWN_ERROR_CODE);
        }
    }

    @Override
    @Transactional
    public ApiResponse deleteUser(String username) {
        if (username == null || username.isEmpty()) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, INVALID_STUDENT_ID_CODE).getLeft().getApiResponse();
        }
        try {
            username = username.strip().toUpperCase();
            if (!userRepository.existsByUsername(username)) {
                log.error("resource not found for username => {}", username);
                return buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE).getLeft().getApiResponse();
            }
            userRepository.deleteByUsername(username);

            HttpStatus httpStatus = HttpStatus.NO_CONTENT;
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-HttpStatus", httpStatus.toString());
            return buildSuccessApiResponse(httpStatus, headers, TRANSACTION_SUCCESS_CODE);
        } catch (DataAccessException e) {
            log.error("delete error => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE).getLeft().getApiResponse();
        } catch (Exception e) {
            log.error("server error => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, UNKNOWN_ERROR_CODE).getLeft().getApiResponse();
        }
    }

    @Override
    public ApiResponse setPassword(String username, char[] password) {
        if (!PasswordValidator.isPasswordStrong(password)) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, WEAK_PASSWORD_CODE).getLeft().getApiResponse();
        }
        if (username == null || username.isEmpty()) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, MISSING_USER_NAME_CODE).getLeft().getApiResponse();
        }
        username = username.strip().toUpperCase();
        try {
            return userRepository.findByUsername(username)
                    .map(user -> {
                        user.setPassword(passwordEncoder.encode(new String(password)).toCharArray());
                        user.setPasswordSet(true);
                        long timeStamp = AppUtil.convertLocalDateTimeToLong(LocalDateTime.now());
                        user.setLastPasswordChangedAt(timeStamp);
                        user.setLastUpdatedAt(timeStamp);
                        user.setLastActionBy(AppUtil.getUserFromSecurityContext());
                        userRepository.save(user);
                        HttpStatus httpStatus = HttpStatus.NO_CONTENT;
                        HttpHeaders headers = new HttpHeaders();
                        headers.add("X-HttpStatus", httpStatus.toString());
                        return buildSuccessApiResponse(httpStatus, headers, TRANSACTION_SUCCESS_CODE);
                    })
                    .orElseGet(() -> buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE)
                            .getLeft()
                            .getApiResponse());
        } catch (DataAccessException e) {
            log.error("find request error => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE).getLeft().getApiResponse();
        } catch (Exception e) {
            log.error("an unknown error occurred => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, UNKNOWN_ERROR_CODE).getLeft().getApiResponse();
        }
    }

    @Override
    public ApiResponse changePassword(String username, char[] currentPassword, char[] newPassword) {
        if (!PasswordValidator.isPasswordStrong(newPassword)) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, WEAK_PASSWORD_CODE).getLeft().getApiResponse();
        }
        if (username == null || username.isEmpty()) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, MISSING_USER_NAME_CODE).getLeft().getApiResponse();
        }
        username = username.strip().toUpperCase();
        try {
            return userRepository.findByUsername(username)
                    .map(user -> {
                        if (!passwordEncoder.matches(new String(currentPassword), new String(user.getPassword()))) {
                            return buildErrorResponse(HttpStatus.BAD_REQUEST, PASSWORD_MISMATCH_CODE)
                                    .getLeft()
                                    .getApiResponse();
                        }
                        user.setPassword(passwordEncoder.encode(new String(newPassword)).toCharArray());
                        user.setPasswordSet(true);
                        long timeStamp = AppUtil.convertLocalDateTimeToLong(LocalDateTime.now());
                        user.setLastPasswordChangedAt(timeStamp);
                        user.setLastUpdatedAt(timeStamp);
                        user.setLastActionBy(AppUtil.getUserFromSecurityContext());
                        userRepository.save(user);
                        HttpStatus httpStatus = HttpStatus.NO_CONTENT;
                        HttpHeaders headers = new HttpHeaders();
                        headers.add("X-HttpStatus", httpStatus.toString());
                        return buildSuccessApiResponse(httpStatus, headers, TRANSACTION_SUCCESS_CODE);
                    })
                    .orElseGet(() -> buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE)
                            .getLeft()
                            .getApiResponse());
        } catch (DataAccessException e) {
            log.error("find request error => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE).getLeft().getApiResponse();
        } catch (Exception e) {
            log.error("an unknown error occurred => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, UNKNOWN_ERROR_CODE).getLeft().getApiResponse();
        }
    }

    @Override
    public ApiResponse updateStatus(String username, UserStatus status) {
        if (username == null || username.isEmpty()) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, MISSING_USER_NAME_CODE).getLeft().getApiResponse();
        }
        username = username.strip().toUpperCase();
        try {
            return userRepository.findByUsername(username)
                    .map(user -> {
                        long timeStamp = AppUtil.convertLocalDateTimeToLong(LocalDateTime.now());
                        user.setStatus(status);
                        user.setLastUpdatedAt(timeStamp);
                        user.setLastActionBy(AppUtil.getUserFromSecurityContext());
                        userRepository.save(user);
                        HttpStatus httpStatus = HttpStatus.NO_CONTENT;
                        HttpHeaders headers = new HttpHeaders();
                        headers.add("X-HttpStatus", httpStatus.toString());
                        return buildSuccessApiResponse(httpStatus, headers, TRANSACTION_SUCCESS_CODE);
                    })
                    .orElseGet(() -> buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE)
                            .getLeft()
                            .getApiResponse());
        } catch (DataAccessException e) {
            log.error("find request error => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE).getLeft().getApiResponse();
        } catch (Exception e) {
            log.error("an unknown error occurred => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, UNKNOWN_ERROR_CODE).getLeft().getApiResponse();
        }
    }

    @Override
    @Transactional
    public ApiResponse addRoles(String username, Set<UserRole> roles) {
        if (username == null || username.isEmpty()) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, MISSING_USER_NAME_CODE).getLeft().getApiResponse();
        }
        username = username.strip().toUpperCase();
        try {
            return userRepository.findByUsername(username)
                    .map(user -> {
                        long timeStamp = AppUtil.convertLocalDateTimeToLong(LocalDateTime.now());
                        Set<Item> roleItems = getRoleItems(roles);
                        Set<Item> newRoles = roles.stream().map(r -> new Item(r.name())).collect(Collectors.toSet());
                        Set<Item> combinedSet = new HashSet<>(roleItems);
                        combinedSet.addAll(user.getRoles());
                        combinedSet.addAll(newRoles);
                        user.setRoles(combinedSet);
                        user.setLastUpdatedAt(timeStamp);
                        user.setLastActionBy(AppUtil.getUserFromSecurityContext());
                        User savedUser = userRepository.save(user);
                        HttpStatus httpStatus = HttpStatus.NO_CONTENT;
                        HttpHeaders headers = new HttpHeaders();
                        headers.add("X-HttpStatus", httpStatus.toString());
                        return buildSuccessApiResponse(httpStatus, headers, TRANSACTION_SUCCESS_CODE);
                    })
                    .orElseGet(() -> buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE)
                            .getLeft()
                            .getApiResponse());
        } catch (DataAccessException e) {
            log.error("find request error => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE).getLeft().getApiResponse();
        } catch (Exception e) {
            log.error("an unknown error occurred => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, UNKNOWN_ERROR_CODE).getLeft().getApiResponse();
        }
    }

    @Transactional
    @Override
    public ApiResponse removeAllRoles(String username) {
        if (username == null || username.isEmpty()) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, MISSING_USER_NAME_CODE).getLeft().getApiResponse();
        }
        username = username.strip().toUpperCase();
        try {
            return userRepository.findByUsername(username)
                    .map(user -> {
                        long timeStamp = AppUtil.convertLocalDateTimeToLong(LocalDateTime.now());
                        Set<Item> roles = new HashSet<>();
                        user.setRoles(roles);
                        user.setLastUpdatedAt(timeStamp);
                        user.setLastActionBy(AppUtil.getUserFromSecurityContext());
                        User savedUser = userRepository.save(user);
                        HttpStatus httpStatus = HttpStatus.NO_CONTENT;
                        HttpHeaders headers = new HttpHeaders();
                        headers.add("X-HttpStatus", httpStatus.toString());
                        return buildSuccessApiResponse(httpStatus, headers, TRANSACTION_SUCCESS_CODE);
                    })
                    .orElseGet(() -> buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE)
                            .getLeft()
                            .getApiResponse());
        } catch (DataAccessException e) {
            log.error("find request error => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE).getLeft().getApiResponse();
        } catch (Exception e) {
            log.error("an unknown error occurred => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, UNKNOWN_ERROR_CODE).getLeft().getApiResponse();
        }
    }

    @Override
    @Transactional
    public ApiResponse removeRole(String username, UserRole role) {
        if (username == null || username.isEmpty()) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, MISSING_USER_NAME_CODE).getLeft().getApiResponse();
        }
        username = username.strip().toUpperCase();
        try {
            return userRepository.findByUsername(username)
                    .map(user -> {
                        long timeStamp = AppUtil.convertLocalDateTimeToLong(LocalDateTime.now());
                        user.getRoles().removeIf(r -> r.getName().equalsIgnoreCase(role.name()));
                        user.setLastUpdatedAt(timeStamp);
                        user.setLastActionBy(AppUtil.getUserFromSecurityContext());
                        User savedUser = userRepository.save(user);
                        HttpStatus httpStatus = HttpStatus.NO_CONTENT;
                        HttpHeaders headers = new HttpHeaders();
                        headers.add("X-HttpStatus", httpStatus.toString());
                        return buildSuccessApiResponse(httpStatus, headers, TRANSACTION_SUCCESS_CODE);
                    })
                    .orElseGet(() -> buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE)
                            .getLeft()
                            .getApiResponse());
        } catch (DataAccessException e) {
            log.error("find request error => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE).getLeft().getApiResponse();
        } catch (Exception e) {
            log.error("an unknown error occurred => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, UNKNOWN_ERROR_CODE).getLeft().getApiResponse();
        }
    }

    /**
     * Retrieves the items associated with the provided set of user roles.
     * If the roles do not already exist in the database, they are created.
     *
     * @param roles a set of user roles to be used for retrieving items
     * @return a set of items associated with the provided user roles
     * @throws RuntimeException if an error occurs while retrieving or creating roles
     */
    @Transactional
    protected Set<Item> getRoleItems(Set<UserRole> roles) {
        return createRolesIfNotAlreadyExisting(roles.stream()
                .map(role -> new Item(role.name()))
                .collect(Collectors.toSet()));
    }


    /**
     * Creates a new user entity based on the provided {@code createUserRequest}.
     * If the username already exists in the database, returns a failure response with a
     * {@link HttpStatus#BAD_REQUEST} status code and {@link #DUPLICATE_PERSISTENCE_ERROR_CODE}.
     * If the user entity is successfully created, returns a success response with the user details
     * in the response body, a {@link HttpStatus#CREATED} status code, and {@link #TRANSACTION_CREATED_CODE}.
     * Handles various persistence-related exceptions and logs appropriate error messages.
     *
     * @param createUserRequest the request object containing user details to be created
     * @return an {@link Either} instance containing either a {@link FailureResponse} if an error occurs
     * or a {@link SuccessResponse} if the user entity is successfully created
     * @see User
     * @see CreateUserRequest
     * @see FailureResponse
     * @see SuccessResponse
     * @see HttpStatus
     * @see #DUPLICATE_PERSISTENCE_ERROR_CODE
     * @see #TRANSACTION_CREATED_CODE
     */
    private Either<FailureResponse, SuccessResponse> createUserEntity(CreateUserRequest createUserRequest) {

        try {
            User user = getUserEntity(createUserRequest);

            if (userRepository.existsByUsername(user.getUsername())) {
                log.error("Data integrity violation error, username: {} already exist", user.getUsername());
                return buildErrorResponse(HttpStatus.BAD_REQUEST, DUPLICATE_PERSISTENCE_ERROR_CODE);
            }
            user = userRepository.save(user);

            return buildSuccessResponse(objectMapper.valueToTree(UserToUserResponseMapper.INSTANCE.toUserResponse(user)),
                    HttpStatus.CREATED, TRANSACTION_CREATED_CODE);

        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation error: {}", e.getMessage());
            return buildErrorResponse(HttpStatus.BAD_REQUEST, DUPLICATE_PERSISTENCE_ERROR_CODE);
        } catch (DataAccessResourceFailureException e) {
            log.error("Database connection error: {}", e.getMessage());
            return buildErrorResponse(HttpStatus.BAD_REQUEST, DATA_PERSISTENCE_ERROR_CODE);
        } catch (DataAccessException e) {
            log.error("Persistence error: {}", e.getMessage());
            return buildErrorResponse(HttpStatus.BAD_REQUEST, DATA_PERSISTENCE_ERROR_CODE);
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}", e.getMessage());
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, UNKNOWN_ERROR_CODE);
        }
    }

    /**
     * Converts the provided {@code createUserRequest} to a {@link User} entity.
     * Uses the {@link CreateUserRequestToUserMapper} to map the fields from the request to the user entity.
     * Sets the user's status to {@link UserStatus#CREATED}, sets the creation and last update timestamps to the current time,
     * and returns the created user entity.
     *
     * @param createUserRequest the request object containing user details
     * @return the created {@link User} entity with mapped fields and default status and timestamps
     * @see User
     * @see CreateUserRequest
     * @see CreateUserRequestToUserMapper
     * @see UserStatus
     * @see AppUtil#convertLocalDateTimeToLong(LocalDateTime)
     */
    private User getUserEntity(CreateUserRequest createUserRequest) {
        User user = CreateUserRequestToUserMapper.INSTANCE.toUser(createUserRequest);
        long now = convertLocalDateTimeToLong(LocalDateTime.now());
        user.setStatus(UserStatus.CREATED);
        user.setCreatedAt(now);
        user.setLastUpdatedAt(now);
        user.setUsername(user.getUsername().strip().toUpperCase());
        if (user.getRoles() != null || !user.getRoles().isEmpty()) {
            createRolesIfNotAlreadyExisting(user.getRoles());
        }
        return user;
    }

    /**
     * Creates roles based on the provided set of items if they do not already exist in the database.
     *
     * @param roles a set of items representing roles to be created if they do not exist
     * @return a set of roles created or retrieved from the database
     */
    private Set<Item> createRolesIfNotAlreadyExisting(Set<Item> roles) {
        Set<Role> roleSet = roles.stream()
                .filter(role -> !roleRepository.existsByName(role.getName()))
                .map(role -> {
                    Role newRole = new Role();
                    newRole.setName(role.getName());
                    return newRole;
                })
                .collect(Collectors.toSet());
        if (!roleSet.isEmpty()) {
            List<Role> roleList = roleRepository.saveAll(roleSet);
            log.info("{} new Roles Added: {}", roleList.size(), roleList);
        }
        return roleSet.stream()
                .map(role -> new Item(role.getName()))
                .collect(Collectors.toSet());
    }
}
