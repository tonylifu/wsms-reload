package com.lifu.wsms.reload.service.student;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifu.wsms.reload.api.AppUtil;
import com.lifu.wsms.reload.api.ErrorCode;
import com.lifu.wsms.reload.api.StudentService;
import com.lifu.wsms.reload.api.SuccessCode;
import com.lifu.wsms.reload.dto.request.student.CreateStudentRequest;
import com.lifu.wsms.reload.dto.request.student.UpdateStudentRequest;
import com.lifu.wsms.reload.dto.response.ApiResponse;
import com.lifu.wsms.reload.dto.response.FailureResponse;
import com.lifu.wsms.reload.dto.response.SuccessResponse;
import com.lifu.wsms.reload.mapper.StudentMapper;
import com.lifu.wsms.reload.mapper.StudentToStudentResponseMapper;
import com.lifu.wsms.reload.repository.StudentRepository;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;

import static com.lifu.wsms.reload.api.AppUtil.BAD_REQUEST_INVALID_PARAMS_CODE;
import static com.lifu.wsms.reload.api.AppUtil.TRANSACTION_CREATED_CODE;

@RequiredArgsConstructor
@Slf4j
public class StudentRecord implements StudentService {
    private final StudentRepository studentRepository;
    private final ObjectMapper objectMapper;
    @Override
    public Either<FailureResponse, SuccessResponse> createStudent(CreateStudentRequest createStudentRequest) {
        try {
            return StudentRecordService.validateCreateStudent(createStudentRequest)
                    .map(result -> studentRepository.save(StudentMapper.INSTANCE.toStudent(createStudentRequest)))
                    .map(student -> SuccessResponse.builder()
                            .body(objectMapper.valueToTree(student))
                            .apiResponse(ApiResponse.builder()
                                    .httpStatusCode(HttpStatus.CREATED)
                                    .responseCode(TRANSACTION_CREATED_CODE)
                                    .responseMessage(SuccessCode.getMessageByCode(TRANSACTION_CREATED_CODE))
                                    .isError(false)
                                    .build())
                            .build());
        } catch (DataAccessException e) {
            log.error("persistence error => {}", e.getMessage());
            return Either.left(FailureResponse.builder()
                    .apiResponse(ApiResponse.builder()
                            .isError(true)
                            .httpStatusCode(HttpStatus.BAD_REQUEST)
                            .responseCode(BAD_REQUEST_INVALID_PARAMS_CODE)
                            .responseMessage(ErrorCode.getMessageByCode(BAD_REQUEST_INVALID_PARAMS_CODE))
                            .build())
                    .build());
        }
    }

    @Override
    public Either<FailureResponse, SuccessResponse> findStudent(String studentId) {
        return null;
    }

    @Override
    public Either<FailureResponse, SuccessResponse> updateStudent(UpdateStudentRequest updateStudentRequest) {
        return null;
    }

    @Override
    public ApiResponse deleteStudent(String studentId) {
        return null;
    }
}
