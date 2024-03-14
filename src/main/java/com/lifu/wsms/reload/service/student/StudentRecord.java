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

import java.time.LocalDate;

import static com.lifu.wsms.reload.api.AppUtil.*;

@RequiredArgsConstructor
@Slf4j
public class StudentRecord implements StudentService {
    private final StudentRepository studentRepository;
    private final ObjectMapper objectMapper;
    @Override
    public Either<FailureResponse, SuccessResponse> createStudent(CreateStudentRequest createStudentRequest) {
        try {
            return StudentRecordService.validateCreateStudent(createStudentRequest)
                    .map(result -> {
                        var student = StudentMapper.INSTANCE.toStudent(createStudentRequest);
                        student.setCreatedAt(LocalDate.now());
                        student.setLastUpdateAt(LocalDate.now());
                        return studentRepository.save(student);
                    })
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
                            .responseCode(DATA_PERSISTENCE_ERROR_CODE)
                            .responseMessage(ErrorCode.getMessageByCode(DATA_PERSISTENCE_ERROR_CODE))
                            .build())
                    .build());
        }
    }

    @Override
    public Either<FailureResponse, SuccessResponse> findStudent(String studentId) {
        try {
            return Either.right(
                    studentRepository.findByStudentId(studentId)
                            .map(student -> SuccessResponse.builder()
                                    .body(objectMapper.valueToTree(student))
                                    .apiResponse(ApiResponse.builder()
                                            .httpStatusCode(HttpStatus.OK)
                                            .responseCode(TRANSACTION_OKAY_CODE)
                                            .responseMessage(SuccessCode.getMessageByCode(TRANSACTION_OKAY_CODE))
                                            .isError(false)
                                            .build())
                                    .build())
                            .orElseThrow(() -> new RuntimeException("request failed"))
            );
        } catch (Exception e) {
            log.error("Failed request => {}", e.getMessage());
            return Either.left(FailureResponse.builder()
                    .apiResponse(ApiResponse.builder()
                            .isError(true)
                            .httpStatusCode(HttpStatus.NOT_FOUND)
                            .responseCode(RESOURCE_NOT_FOUND_CODE)
                            .responseMessage(ErrorCode.getMessageByCode(RESOURCE_NOT_FOUND_CODE))
                            .build())
                    .build());
        }
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
