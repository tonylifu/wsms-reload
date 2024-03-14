package com.lifu.wsms.reload.service.student;

import com.lifu.wsms.reload.api.AppUtil;
import com.lifu.wsms.reload.api.ErrorCode;
import com.lifu.wsms.reload.dto.request.student.CreateStudentRequest;
import com.lifu.wsms.reload.dto.request.student.UpdateStudentRequest;
import com.lifu.wsms.reload.dto.response.ApiResponse;
import com.lifu.wsms.reload.dto.response.FailureResponse;
import io.vavr.control.Either;
import org.springframework.http.HttpStatus;

import static com.lifu.wsms.reload.api.AppUtil.*;

public class StudentRecordService {
    private StudentRecordService(){}

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

        if (createStudentRequest.getStudentId() == null || createStudentRequest.getStudentId().isEmpty()
        || !AppUtil.isValidStudentId(createStudentRequest.getStudentId())) {
            return Either.left(FailureResponse.builder()
                    .apiResponse(ApiResponse.builder()
                            .isError(true)
                            .httpStatusCode(HttpStatus.BAD_REQUEST)
                            .responseCode(INVALID_STUDENT_ID_CODE)
                            .responseMessage(ErrorCode.getMessageByCode(INVALID_STUDENT_ID_CODE))
                            .build())
                    .build());
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

        if (!(createStudentRequest.getDob() > 0)) {
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

        if (updateStudentRequest.getStudentId() == null || updateStudentRequest.getStudentId().isEmpty()
                || !AppUtil.isValidStudentId(updateStudentRequest.getStudentId())) {
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

    public static Either<FailureResponse, Boolean> validateStudentId(String studentId) {
        return Either.left(FailureResponse.builder()
                .apiResponse(ApiResponse.builder()
                        .isError(true)
                        .httpStatusCode(HttpStatus.BAD_REQUEST)
                        .responseCode("")
                        .responseMessage("not implemented")
                        .build())
                .build());
    }
}
