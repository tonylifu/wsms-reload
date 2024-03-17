package com.lifu.wsms.reload.service.student;

import com.lifu.wsms.reload.api.AppUtil;
import com.lifu.wsms.reload.api.ErrorCode;
import com.lifu.wsms.reload.dto.request.student.CreateStudentRequest;
import com.lifu.wsms.reload.dto.request.student.UpdateStudentRequest;
import com.lifu.wsms.reload.dto.response.ApiResponse;
import com.lifu.wsms.reload.dto.response.FailureResponse;
import com.lifu.wsms.reload.dto.response.finance.StudentAccountBalanceResponse;
import com.lifu.wsms.reload.entity.finance.AccountBalance;
import com.lifu.wsms.reload.entity.student.Student;
import com.lifu.wsms.reload.mapper.StudentToStudentResponseMapper;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.lifu.wsms.reload.api.AppUtil.*;

@Slf4j
public class StudentRecordService {
    private StudentRecordService(){}

    /**
     * Validates the fields of a {@link CreateStudentRequest} object and returns an {@link Either} indicating success or failure.
     * If the create student request is null or contains invalid parameters, a failure response is returned.
     *
     * @param createStudentRequest The {@link CreateStudentRequest} object to validate.
     * @return An {@link Either} containing a {@link FailureResponse} if validation fails, or a Boolean indicating success.
     */
    public static Either<FailureResponse, Boolean> validateCreateStudent(CreateStudentRequest createStudentRequest) {
        if (createStudentRequest == null) {
            return Either.left(FailureResponse.builder()
                    .apiResponse(ApiResponse.builder()
                            .isError(true)
                            .httpStatusCode(HttpStatus.BAD_REQUEST)
                            .responseCode(BAD_REQUEST_INVALID_PARAMS_CODE)
                            .responseMessage(ErrorCode.getMessageByCode(BAD_REQUEST_INVALID_PARAMS_CODE))
                            .build())
                    .build());
        }

        var isValidStudentId = validateStudentId(createStudentRequest.getStudentId());
        if (isValidStudentId.isLeft()) {
            return Either.left(isValidStudentId.getLeft());
        }

        if (createStudentRequest.getFirstName() == null || createStudentRequest.getFirstName().isEmpty()
        || createStudentRequest.getLastName() == null || createStudentRequest.getLastName().isEmpty()) {
            return Either.left(FailureResponse.builder()
                    .apiResponse(ApiResponse.builder()
                            .isError(true)
                            .httpStatusCode(HttpStatus.BAD_REQUEST)
                            .responseCode(MISSING_NAMES_CODE)
                            .responseMessage(ErrorCode.getMessageByCode(MISSING_NAMES_CODE))
                            .build())
                    .build());
        }

        if (!(AppUtil.isValidLocalDateString(createStudentRequest.getDob()))
                || !(AppUtil.isParseableLocalDateString(createStudentRequest.getDob()))) {
            return Either.left(FailureResponse.builder()
                    .apiResponse(ApiResponse.builder()
                            .isError(true)
                            .httpStatusCode(HttpStatus.BAD_REQUEST)
                            .responseCode(INVALID_DOB_CODE)
                            .responseMessage(ErrorCode.getMessageByCode(INVALID_DOB_CODE))
                            .build())
                    .build());
        }

        // TODO - Add more validation rules as needed

        // If all validations pass, return success
        return Either.right(true);
    }

    /**
     * Validates the fields of an {@link UpdateStudentRequest} object and returns an {@link Either} indicating success or failure.
     * If the update student request is null or contains invalid parameters, a failure response is returned.
     *
     * @param updateStudentRequest The {@link UpdateStudentRequest} object to validate.
     * @return An {@link Either} containing a {@link FailureResponse} if validation fails, or a Boolean indicating success.
     */
    public static Either<FailureResponse, Boolean> validateUpdateStudent(UpdateStudentRequest updateStudentRequest) {
        if (updateStudentRequest == null) {
            return Either.left(FailureResponse.builder()
                    .apiResponse(ApiResponse.builder()
                            .isError(true)
                            .httpStatusCode(HttpStatus.BAD_REQUEST)
                            .responseCode(BAD_REQUEST_INVALID_PARAMS_CODE)
                            .responseMessage(ErrorCode.getMessageByCode(BAD_REQUEST_INVALID_PARAMS_CODE))
                            .build())
                    .build());
        }

        var isValidStudentId = validateStudentId(updateStudentRequest.getStudentId());
        if (isValidStudentId.isLeft()) {
            return Either.left(isValidStudentId.getLeft());
        }

        return Either.right(true);
    }

    /**
     * Validates the provided student ID string.
     * If the student ID is null, empty, or invalid, a failure response is returned.
     *
     * @param studentId The student ID string to validate.
     * @return An {@link Either} containing a {@link FailureResponse} if validation fails, or a Boolean indicating success.
     */
    public static Either<FailureResponse, Boolean> validateStudentId(String studentId) {
        if (studentId == null || studentId.isEmpty() || !AppUtil.isValidStudentId(studentId)) {
            return Either.left(FailureResponse.builder()
                    .apiResponse(ApiResponse.builder()
                            .isError(true)
                            .httpStatusCode(HttpStatus.BAD_REQUEST)
                            .responseCode(INVALID_STUDENT_ID_CODE)
                            .responseMessage(ErrorCode.getMessageByCode(INVALID_STUDENT_ID_CODE))
                            .build())
                    .build());
        }
        return Either.right(true);
    }

    /**
     * Populates a {@link Student} object with the non-null fields from an {@link UpdateStudentRequest} object,
     * and sets the last update date to the current date.
     *
     * @param student              The {@link Student} object to be updated.
     * @param updateStudentRequest The {@link UpdateStudentRequest} containing the new values.
     * @return The updated {@link Student} object.
     */
    public static Student populateStudentForUpdate(Student student, UpdateStudentRequest updateStudentRequest) {
        // Set last update date
        student.setLastUpdateAt(AppUtil.convertLocalDateToLong(LocalDate.now()));

        // Transfer non-null fields from updateStudentRequest to student
        if (updateStudentRequest.getFirstName() != null) {
            student.setFirstName(updateStudentRequest.getFirstName());
        }
        if (updateStudentRequest.getMiddleName() != null) {
            student.setMiddleName(updateStudentRequest.getMiddleName());
        }
        if (updateStudentRequest.getLastName() != null) {
            student.setLastName(updateStudentRequest.getLastName());
        }
        if (AppUtil.isValidLocalDateString(updateStudentRequest.getDob())
                || AppUtil.isParseableLocalDateString(updateStudentRequest.getDob())) {
            student.setDob(AppUtil.convertLocalDateToLong(AppUtil.parseToLocalDate(updateStudentRequest.getDob())));
        }
        if (updateStudentRequest.getGender() != null) {
            student.setGender(updateStudentRequest.getGender());
        }
        if (updateStudentRequest.getAddress() != null) {
            student.setAddress(updateStudentRequest.getAddress());
        }
        if (updateStudentRequest.getContact() != null) {
            student.setContact(updateStudentRequest.getContact());
        }
        if (updateStudentRequest.getLegalGuardian() != null) {
            student.setLegalGuardian(updateStudentRequest.getLegalGuardian());
        }
        if (updateStudentRequest.getCurrentGrade() != null) {
            student.setCurrentGrade(updateStudentRequest.getCurrentGrade());
        }
        if (updateStudentRequest.isDisabled()) {
            student.setDisabled(true);
        }
        if (updateStudentRequest.getDisabilityDetails() != null) {
            student.setDisabilityDetail(updateStudentRequest.getDisabilityDetails());
        }
        return student;
    }

    /**
     * Constructs a StudentAccountBalanceResponse object from an array of objects containing a Student and an AccountBalance.
     *
     * @param objects an array of objects containing a Student and an AccountBalance
     * @return a StudentAccountBalanceResponse object containing information about the student and their account balance
     * @throws RuntimeException if the array of objects is null or if there is an error while casting objects to Student and AccountBalance
     */
    public static StudentAccountBalanceResponse getStudentAccountBalanceResponseFromObjects(Object[] objects) {
        if (objects == null) {
            throw new RuntimeException("The resource does not exist");
        }
        try {
            Student student = (Student) objects[0];
            BigDecimal accountBalance = (BigDecimal) objects[1];
            return StudentAccountBalanceResponse.builder()
                    .studentResponse(StudentToStudentResponseMapper.INSTANCE.toStudentResponse(student))
                    .accountBalance(accountBalance)
                    .build();
        } catch (Exception e) {
            log.error("Error casting object to StudentAccountBalanceResponse: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Converts a paged list of object arrays into a list of StudentAccountBalanceResponse objects.
     * Each object array should contain data representing a Student entity and its associated account balance.
     *
     * @param pagedObjects A paged list of object arrays, where each object array contains data
     *                     representing a Student entity and its associated account balance.
     * @return A list of StudentAccountBalanceResponse objects representing the converted data.
     * @throws RuntimeException if the provided pagedObjects is null or empty, indicating that the resource does not exist.
     */
    public static List<StudentAccountBalanceResponse> getStudentAccountBalanceResponseFromObjectList(Page<Object[]> pagedObjects) {
        if (pagedObjects == null) {
            throw new RuntimeException("The resource does not exist");
        }
        List<StudentAccountBalanceResponse> result = new ArrayList<>();
        for (Object[] object : pagedObjects) {
            result.add(getStudentAccountBalanceResponseFromObjects(object));
        }
        return result;
    }

}
