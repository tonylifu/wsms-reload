package com.lifu.wsms.reload.service.student;

import com.lifu.wsms.reload.dto.request.student.CreateStudentRequest;
import com.lifu.wsms.reload.dto.request.student.UpdateStudentRequest;
import com.lifu.wsms.reload.dto.response.ApiResponse;
import com.lifu.wsms.reload.dto.response.FailureResponse;
import io.vavr.control.Either;
import org.springframework.http.HttpStatus;

public class StudentRecordService {
    private StudentRecordService(){}

    public static Either<FailureResponse, Boolean> validateCreateStudent(CreateStudentRequest createStudentRequest) {
        return Either.left(FailureResponse.builder()
                        .apiResponse(ApiResponse.builder()
                                .isError(true)
                                .httpStatusCode(HttpStatus.BAD_REQUEST)
                                .responseCode("")
                                .responseMessage("not implemented")
                                .build())
                .build());
    }

    public static Either<FailureResponse, Boolean> validateUpdateStudent(UpdateStudentRequest updateStudentRequest) {
        return Either.left(FailureResponse.builder()
                .apiResponse(ApiResponse.builder()
                        .isError(true)
                        .httpStatusCode(HttpStatus.BAD_REQUEST)
                        .responseCode("")
                        .responseMessage("not implemented")
                        .build())
                .build());
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
