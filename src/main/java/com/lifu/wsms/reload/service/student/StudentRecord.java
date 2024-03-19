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
import com.lifu.wsms.reload.dto.response.finance.StudentAccountBalanceResponse;
import com.lifu.wsms.reload.dto.response.finance.StudentAccountBalanceResponses;
import com.lifu.wsms.reload.dto.response.student.StudentResponse;
import com.lifu.wsms.reload.dto.response.student.StudentResponses;
import com.lifu.wsms.reload.entity.finance.AccountBalance;
import com.lifu.wsms.reload.entity.student.Student;
import com.lifu.wsms.reload.mapper.CreateStudentRequestToStudentMapper;
import com.lifu.wsms.reload.mapper.StudentToStudentResponseMapper;
import com.lifu.wsms.reload.repository.AccountRepository;
import com.lifu.wsms.reload.repository.StudentRepository;
import com.lifu.wsms.reload.service.ApiService;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.lifu.wsms.reload.api.AppUtil.*;
import static com.lifu.wsms.reload.service.ApiService.buildErrorResponse;
import static com.lifu.wsms.reload.service.ApiService.buildSuccessResponse;

@RequiredArgsConstructor
@Slf4j
public class StudentRecord implements StudentService {
    private final StudentRepository studentRepository;
    private final AccountRepository accountRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    @Override
    public Either<FailureResponse, SuccessResponse> createStudent(CreateStudentRequest createStudentRequest) {
        if (createStudentRequest.getStudentId() != null) {
            createStudentRequest.setStudentId(createStudentRequest.getStudentId().strip().toUpperCase());
        }
        return ApiService.validateCreateStudent(createStudentRequest)
                .fold(Either::left, validatedRequest -> createStudentAndAccount(createStudentRequest));
    }

    @Override
    public Either<FailureResponse, SuccessResponse> findStudent(String studentId) {
        try {
            return Either.right(
                    studentRepository.findByStudentId(studentId.strip().toUpperCase())
                            .map(student -> buildSuccessResponse(objectMapper.valueToTree(StudentToStudentResponseMapper.INSTANCE.toStudentResponse(student)),
                                    HttpStatus.OK, TRANSACTION_OKAY_CODE).get())
                            .orElseThrow(() -> new RuntimeException("request failed"))
            );
        } catch (DataAccessException e) {
            log.error("update request error => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE);
        } catch (Exception e) {
            log.error("an unknown error occurred => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, UNKNOWN_ERROR_CODE);
        }
    }

    @Override
    public Either<FailureResponse, SuccessResponse> updateStudent(UpdateStudentRequest updateStudentRequest) {
        try {
            if (updateStudentRequest.getStudentId() != null) {
                updateStudentRequest.setStudentId(updateStudentRequest.getStudentId().strip().toUpperCase());
            }
            return ApiService.validateUpdateStudent(updateStudentRequest)
                    .map(result -> {
                        return studentRepository.findByStudentId(updateStudentRequest.getStudentId())
                                .map(student -> studentRepository.save(ApiService.populateStudentForUpdate(student, updateStudentRequest)))
                                .orElseThrow(() -> new RuntimeException("Student record update failed"));
                    })
                    .map(student -> buildSuccessResponse(objectMapper.valueToTree(StudentToStudentResponseMapper.INSTANCE.toStudentResponse(student)),
                            HttpStatus.OK, TRANSACTION_UPDATED_CODE).get()
                    );
        } catch (DataAccessException e) {
            log.error("update error => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.NO_CONTENT, DATA_PERSISTENCE_ERROR_CODE);
        }
    }

    @Transactional
    @Override
    public ApiResponse deleteStudent(String studentId) {
        try {
            studentId = studentId.strip().toUpperCase();
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
            List<StudentResponse> studentResponses = studentRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent()
                    .stream()
                    .map(StudentToStudentResponseMapper.INSTANCE::toStudentResponse)
                    .toList();
            return buildSuccessResponse(objectMapper.valueToTree(
                            StudentResponses.builder()
                                    .students(studentResponses)
                                    .totalCount(studentRepository.count())
                                    .build()),
                    HttpStatus.OK, TRANSACTION_SUCCESS_CODE);
        } catch (DataAccessException e) {
            log.error("Fetch students request error => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE);
        }
    }

    @Override
    public Either<FailureResponse, SuccessResponse> findStudentAndAccount(String studentId) {
        try {
            return Either.right(
                    studentRepository.findStudentAndAccountBalanceByStudentId(studentId.strip().toUpperCase())
                            .stream()
                            .map(objects -> {
                                StudentAccountBalanceResponse studentAccountBalanceResponse =
                                        ApiService.getStudentAccountBalanceResponseFromObjects(objects);
                                return buildSuccessResponse(objectMapper.valueToTree(studentAccountBalanceResponse),
                                        HttpStatus.OK, TRANSACTION_SUCCESS_CODE).get();
                            })
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("student and balance request failed"))
            );
        } catch (Exception e) {
            log.error("Failed student balance request => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE);
        }
    }

    @Override
    public Either<FailureResponse, SuccessResponse> findAllStudentAndAccounts(int pageNumber, int pageSize) {
        try {
            Page<Object[]> pagedResults = studentRepository.findAllStudentsAndAccountBalances(PageRequest.of(pageNumber, pageSize));
            List<StudentAccountBalanceResponse> studentAccountBalanceResponseList =
                    ApiService.getStudentAccountBalanceResponseFromObjectList(pagedResults);
            var studentAccountBalanceResponses = StudentAccountBalanceResponses.builder()
                    .studentAccounts(studentAccountBalanceResponseList)
                    .totalCount(studentRepository.count())
                    .build();

            return buildSuccessResponse(objectMapper.valueToTree(studentAccountBalanceResponses),
                    HttpStatus.OK, TRANSACTION_SUCCESS_CODE);
        } catch (DataAccessException e) {
            log.error("Fetch students and account request error => {}", e.getMessage());
            return buildErrorResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_CODE);
        }
    }

    /**
     * Creates a new Student entity and its associated AccountBalance entity based on the provided CreateStudentRequest.
     * Saves both entities to the database and returns a success response if successful.
     * If an error occurs during the process, it logs the error and returns an appropriate failure response.
     *
     * @param createStudentRequest The CreateStudentRequest containing the information to create the Student and AccountBalance entities.
     * @return Either a SuccessResponse containing the newly created Student information if successful,
     * or a FailureResponse containing error details if an error occurs during the process.
     */
    private Either<FailureResponse, SuccessResponse> createStudentAndAccount(CreateStudentRequest createStudentRequest) {
        try {
            Student student = createStudentEntity(createStudentRequest);
            AccountBalance accountBalance = createAccountBalanceEntity(student);

            studentRepository.save(student);
            accountRepository.save(accountBalance);

            return buildSuccessResponse(objectMapper.valueToTree(StudentToStudentResponseMapper.INSTANCE.toStudentResponse(student)),
                    HttpStatus.CREATED, TRANSACTION_CREATED_CODE);

        } catch (DataIntegrityViolationException e) {
            log.error("SQL insertion error: {}", e.getMessage());
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
     * Creates a new Student entity based on the provided CreateStudentRequest.
     *
     * @param createStudentRequest The CreateStudentRequest containing the information to create the Student entity.
     * @return The newly created Student entity populated with data from the CreateStudentRequest.
     */
    private Student createStudentEntity(CreateStudentRequest createStudentRequest) {
        Student student = CreateStudentRequestToStudentMapper.INSTANCE.toStudent(createStudentRequest);

        // Set the creation and last update timestamps to the current time
        long now = AppUtil.convertLocalDateToLong(LocalDate.now());
        student.setCreatedAt(now);
        student.setLastUpdateAt(now);

        return student;
    }


    /**
     * Creates a new AccountBalance entity based on the provided Student.
     *
     * @param student The Student object for which the AccountBalance entity will be created.
     * @return The newly created AccountBalance entity initialized with default values and information from the Student.
     */
    private AccountBalance createAccountBalanceEntity(Student student) {
        return AccountBalance.builder()
                .studentId(student.getStudentId())
                .balance(BigDecimal.ZERO)
                .createdAt(student.getCreatedAt())
                .lastUpdateAt(student.getLastUpdateAt())
                .build();
    }

}
