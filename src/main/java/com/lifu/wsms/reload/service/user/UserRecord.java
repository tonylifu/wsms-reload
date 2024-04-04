package com.lifu.wsms.reload.service.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifu.wsms.reload.api.contract.user.UserService;
import com.lifu.wsms.reload.dto.request.user.CreateUserRequest;
import com.lifu.wsms.reload.dto.request.user.UpdateUserRequest;
import com.lifu.wsms.reload.dto.response.ApiResponse;
import com.lifu.wsms.reload.dto.response.FailureResponse;
import com.lifu.wsms.reload.dto.response.SuccessResponse;
import com.lifu.wsms.reload.entity.user.User;
import com.lifu.wsms.reload.enums.UserRole;
import com.lifu.wsms.reload.enums.UserStatus;
import com.lifu.wsms.reload.mapper.user.CreateUserRequestToUserMapper;
import com.lifu.wsms.reload.mapper.user.UserToUserResponseMapper;
import com.lifu.wsms.reload.repository.UserRepository;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import static com.lifu.wsms.reload.api.AppUtil.*;
import static com.lifu.wsms.reload.service.student.StudentApiService.buildErrorResponse;
import static com.lifu.wsms.reload.service.student.StudentApiService.buildSuccessResponse;
import static com.lifu.wsms.reload.service.user.UserApiService.validateCreateUser;

@RequiredArgsConstructor
@Slf4j
public class UserRecord implements UserService {
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public Either<FailureResponse, SuccessResponse> createUser(CreateUserRequest createUserRequest) {
        return validateCreateUser(createUserRequest)
                .fold(Either::left, validatedRequest -> createUserEntity(createUserRequest));
    }

    @Override
    public Either<FailureResponse, SuccessResponse> findUser(String username) {
        return null;
    }

    @Override
    public Either<FailureResponse, SuccessResponse> updateUser(UpdateUserRequest updateUserRequest) {
        return null;
    }

    @Override
    public ApiResponse deleteUser(String username) {
        return null;
    }

    @Override
    public ApiResponse setPassword(String username, char[] password) {
        //TODO the entity User to have isPasswordSet field
        return null;
    }

    @Override
    public ApiResponse changePassword(String username, char[] currentPassword, char[] newPassword) {
        //TODO - user entity to have date of last password change
        return null;
    }

    @Override
    public ApiResponse updateStatus(String username, UserStatus status) {
        return null;
    }

    @Override
    public ApiResponse addRoles(String username, Set<UserRole> roles) {
        return null;
    }

    @Override
    public ApiResponse removeAllRoles(String username) {
        return null;
    }

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

    private User getUserEntity(CreateUserRequest createUserRequest) {
        return CreateUserRequestToUserMapper.INSTANCE.toUser(createUserRequest);
    }
}
