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
import com.lifu.wsms.reload.entity.finance.AccountBalance;
import com.lifu.wsms.reload.mapper.CreateStudentRequestToStudentMapper;
import com.lifu.wsms.reload.mapper.StudentToStudentResponseMapper;
import com.lifu.wsms.reload.repository.AccountRepository;
import com.lifu.wsms.reload.repository.StudentRepository;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Collectors;

import static com.lifu.wsms.reload.api.AppUtil.*;

@RequiredArgsConstructor
@Slf4j
public class StudentRecord implements StudentService {
    private final StudentRepository studentRepository;
    private final AccountRepository accountRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    @Override
    public Either<FailureResponse, SuccessResponse> createStudent(CreateStudentRequest createStudentRequest) {
        try {
            return StudentRecordService.validateCreateStudent(createStudentRequest)
                    .map(result -> {
                        var student = CreateStudentRequestToStudentMapper.INSTANCE.toStudent(createStudentRequest);
                        student.setCreatedAt(AppUtil.convertLocalDateToLong(LocalDate.now()));
                        student.setLastUpdateAt(AppUtil.convertLocalDateToLong(LocalDate.now()));
                        var createdStudent = studentRepository.save(student);
                        //TODO - extract create account to a different class SRP (Student and Account to be atomic)
                        accountRepository.save(AccountBalance.builder()
                                .studentId(createdStudent.getStudentId())
                                .balance(BigDecimal.ZERO)
                                .createdAt(AppUtil.convertLocalDateToLong(LocalDate.now()))
                                .lastUpdateAt(AppUtil.convertLocalDateToLong(LocalDate.now()))
                                .build());
                        return createdStudent;
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
        try {
            return StudentRecordService.validateUpdateStudent(updateStudentRequest)
                    .map(result -> {
                        return studentRepository.findByStudentId(updateStudentRequest.getStudentId())
                                .map(student -> studentRepository.save(StudentRecordService.populateStudentForUpdate(student, updateStudentRequest)))
                                .orElseThrow(() -> new RuntimeException("Student record update failed"));
                    })
                    .map(student -> SuccessResponse.builder()
                            .body(objectMapper.valueToTree(StudentToStudentResponseMapper.INSTANCE.toStudentResponse(student)))
                            .apiResponse(ApiResponse.builder()
                                    .httpStatusCode(HttpStatus.OK)
                                    .responseCode(TRANSACTION_UPDATED_CODE)
                                    .responseMessage(SuccessCode.getMessageByCode(TRANSACTION_UPDATED_CODE))
                                    .isError(false)
                                    .build())
                            .build());
        } catch (DataAccessException e) {
            log.error("update error => {}", e.getMessage());
            return Either.left(FailureResponse.builder()
                    .apiResponse(ApiResponse.builder()
                            .isError(true)
                            .httpStatusCode(HttpStatus.NO_CONTENT)
                            .responseCode(DATA_PERSISTENCE_ERROR_CODE)
                            .responseMessage(ErrorCode.getMessageByCode(DATA_PERSISTENCE_ERROR_CODE))
                            .build())
                    .build());
        }
    }

    @Transactional
    @Override
    public ApiResponse deleteStudent(String studentId) {
        try {
            studentRepository.deleteByStudentId(studentId);
            accountRepository.deleteByStudentId(studentId);
            return ApiResponse.builder()
                    .isError(false)
                    .httpStatusCode(HttpStatus.NO_CONTENT)
                    .responseCode(TRANSACTION_SUCCESS_CODE)
                    .responseMessage(SuccessCode.getMessageByCode(TRANSACTION_SUCCESS_CODE))
                    .build();
        } catch (DataAccessException e) {
            log.error("delete error => {}", e.getMessage());
            return ApiResponse.builder()
                    .isError(true)
                    .httpStatusCode(HttpStatus.NOT_FOUND)
                    .responseCode(RESOURCE_NOT_FOUND_CODE)
                    .responseMessage(ErrorCode.getMessageByCode(RESOURCE_NOT_FOUND_CODE))
                    .build();
        }
    }

    @Override
    public Either<FailureResponse, SuccessResponse> findAllStudents(int pageNumber, int pageSize) {
        try {
            return Either.right(
                    SuccessResponse.builder()
                            .body(AppUtil.convertListToJsonNode(
                                    studentRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent()
                                            .stream()
                                            .map(StudentToStudentResponseMapper.INSTANCE::toStudentResponse)
                                            .collect(Collectors.toList())
                            )).apiResponse(ApiResponse.builder()
                                    .isError(false)
                                    .httpStatusCode(HttpStatus.OK)
                                    .responseCode(TRANSACTION_SUCCESS_CODE)
                                    .responseMessage(SuccessCode.getMessageByCode(TRANSACTION_SUCCESS_CODE))
                                    .build())
                            .build()
            );
        } catch (DataAccessException e) {
            log.error("Fetch students request error => {}", e.getMessage());
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
}
