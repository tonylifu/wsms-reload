package com.lifu.wsms.reload.controller.student;

import com.lifu.wsms.reload.api.contract.student.StudentService;
import com.lifu.wsms.reload.dto.request.student.CreateStudentRequest;
import com.lifu.wsms.reload.dto.request.student.UpdateStudentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class StudentController {
    private final StudentService studentService;
    public static final String STUDENT_PATH = "/api/v1/students";
    public static final String STUDENT_PATH_ID = STUDENT_PATH + "/{studentId}";
    public static final String STUDENT_ACCOUNT_PATH = "/api/v1/accounts";
    public static final String STUDENT_ACCOUNT_PATH_ID = STUDENT_ACCOUNT_PATH + "/{studentId}";

    @Validated
    @PostMapping(STUDENT_PATH)
    public ResponseEntity<?> createStudent(@Valid @RequestBody final CreateStudentRequest studentRequest) {
        return studentService.createStudent(studentRequest)
                .fold(
                        failureResponse -> ResponseEntity
                                .status(failureResponse.getApiResponse().getHttpStatusCode())
                                .headers(failureResponse.getApiResponse().getHttpHeaders())
                                .body(failureResponse),
                        successResponse -> ResponseEntity
                                .status(successResponse.getApiResponse().getHttpStatusCode())
                                .headers(successResponse.getApiResponse().getHttpHeaders())
                                .header("location", STUDENT_PATH + "/" +
                                        successResponse.getBody().get("studentId").asText())
                                .body(successResponse)
                );
    }

    @PutMapping(STUDENT_PATH)
    public ResponseEntity<?> updateStudent(@RequestBody final UpdateStudentRequest studentRequest) {
        return studentService.updateStudent(studentRequest)
                .fold(
                        failureResponse -> ResponseEntity
                                .status(failureResponse.getApiResponse().getHttpStatusCode())
                                .headers(failureResponse.getApiResponse().getHttpHeaders())
                                .body(failureResponse),
                        successResponse -> ResponseEntity
                                .status(successResponse.getApiResponse().getHttpStatusCode())
                                .headers(successResponse.getApiResponse().getHttpHeaders())
                                .header("location", STUDENT_PATH + "/" +
                                        successResponse.getBody().get("studentId").asText())
                                .body(successResponse)
                );
    }

    @GetMapping(STUDENT_PATH_ID)
    public ResponseEntity<?> findStudent(@PathVariable("studentId") String studentId) {
        return studentService.findStudent(studentId)
                .fold(
                        failureResponse -> ResponseEntity
                                .status(failureResponse.getApiResponse().getHttpStatusCode())
                                .headers(failureResponse.getApiResponse().getHttpHeaders())
                                .body(failureResponse),
                        successResponse -> ResponseEntity
                                .status(successResponse.getApiResponse().getHttpStatusCode())
                                .headers(successResponse.getApiResponse().getHttpHeaders())
                                .header("location", STUDENT_PATH + "/" +
                                        successResponse.getBody().get("studentId").asText())
                                .body(successResponse)
                );
    }

    @DeleteMapping(STUDENT_PATH_ID)
    public ResponseEntity<?> deleteStudent(@PathVariable("studentId") final String studentId) {
        var deleteResponse = studentService.deleteStudent(studentId);
        return ResponseEntity
                .status(deleteResponse.getHttpStatusCode())
                .headers(deleteResponse.getHttpHeaders())
                .body(deleteResponse);
    }

    @GetMapping(STUDENT_PATH)
    public ResponseEntity<?> findAllStudents(@RequestParam(defaultValue = "0") int pageNumber,
                                             @RequestParam(defaultValue = "10") int pageSize) {
        return studentService.findAllStudents(pageNumber, pageSize)
                .fold(
                        failureResponse -> ResponseEntity
                                .status(failureResponse.getApiResponse().getHttpStatusCode())
                                .headers(failureResponse.getApiResponse().getHttpHeaders())
                                .body(failureResponse),
                        successResponse -> ResponseEntity
                                .status(successResponse.getApiResponse().getHttpStatusCode())
                                .headers(successResponse.getApiResponse().getHttpHeaders())
                                .header("location", STUDENT_PATH)
                                .body(successResponse)
                );
    }

    @GetMapping(STUDENT_ACCOUNT_PATH_ID)
    public ResponseEntity<?> findStudentAccountBalance(@PathVariable("studentId") String studentId) {
        return studentService.findStudentAndAccount(studentId)
                .fold(
                        failureResponse -> ResponseEntity
                                .status(failureResponse.getApiResponse().getHttpStatusCode())
                                .headers(failureResponse.getApiResponse().getHttpHeaders())
                                .body(failureResponse),
                        successResponse -> ResponseEntity
                                .status(successResponse.getApiResponse().getHttpStatusCode())
                                .headers(successResponse.getApiResponse().getHttpHeaders())
                                .header("location", STUDENT_ACCOUNT_PATH +
                                        successResponse.getBody().get("studentResponse").get("studentId"))
                                .body(successResponse)
                );
    }

    @GetMapping(STUDENT_ACCOUNT_PATH)
    public ResponseEntity<?> findAllStudentsAndAccountBalances(@RequestParam(defaultValue = "0") int pageNumber,
                                             @RequestParam(defaultValue = "10") int pageSize) {
        return studentService.findAllStudentAndAccounts(pageNumber, pageSize)
                .fold(
                        failureResponse -> ResponseEntity
                                .status(failureResponse.getApiResponse().getHttpStatusCode())
                                .headers(failureResponse.getApiResponse().getHttpHeaders())
                                .body(failureResponse),
                        successResponse -> ResponseEntity
                                .status(successResponse.getApiResponse().getHttpStatusCode())
                                .headers(successResponse.getApiResponse().getHttpHeaders())
                                .header("location", STUDENT_ACCOUNT_PATH)
                                .body(successResponse)
                );
    }
}
