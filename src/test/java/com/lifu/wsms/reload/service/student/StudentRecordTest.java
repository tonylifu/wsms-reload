package com.lifu.wsms.reload.service.student;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.lifu.wsms.reload.api.AppUtil;
import com.lifu.wsms.reload.api.contract.student.StudentService;
import com.lifu.wsms.reload.dto.request.student.CreateStudentRequest;
import com.lifu.wsms.reload.dto.request.student.UpdateStudentRequest;
import com.lifu.wsms.reload.dto.response.ApiResponse;
import com.lifu.wsms.reload.dto.response.FailureResponse;
import com.lifu.wsms.reload.dto.response.SuccessResponse;
import com.lifu.wsms.reload.dto.response.finance.StudentAccountBalanceResponse;
import com.lifu.wsms.reload.dto.response.student.StudentResponse;
import com.lifu.wsms.reload.repository.StudentRepository;
import com.lifu.wsms.reload.util.SudentTestUtil;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class StudentRecordTest {
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentRepository studentRepository;

    /**
     * Happy path
     */
    @Transactional
    @Test
    void createReadUpdateAndDelete_Student() {
        // Create Student Request Object
        var createStudentRequest = getCreateStudentRequest();

        // Create Student
        Either<FailureResponse, SuccessResponse> createResponse = studentService.createStudent(createStudentRequest);
        assertTrue(createResponse.isRight());
        var createStudentResponse = createResponse.get();
        assertEquals(HttpStatus.CREATED, createResponse.get().getApiResponse().getHttpStatusCode());
        var studentId = createStudentResponse.getBody().get("studentId").asText();

        // Read Student
        Either<FailureResponse, SuccessResponse> readResponse = studentService.findStudent(studentId);
        assertTrue(readResponse.isRight());
        var readStudentResponse = readResponse.get().getBody();
        assertEquals(HttpStatus.OK, readResponse.get().getApiResponse().getHttpStatusCode());
        assertEquals(studentId, readStudentResponse.get("studentId").asText());
        assertEquals("David", readStudentResponse.get("firstName").asText());

        // Update Student
        var updateStudentRequest = getUpdateStudentRequest();
        updateStudentRequest.setStudentId(studentId);
        Either<FailureResponse, SuccessResponse> updateResponse = studentService.updateStudent(updateStudentRequest);
        assertFalse(updateResponse.isLeft());
        var updateResult = updateResponse.get().getBody();
        assertEquals(studentId, updateResult.get("studentId").asText());
        assertEquals("Joan", updateResult.get("firstName").asText());

        // Delete Student
        ApiResponse deleteStudent = studentService.deleteStudent(studentId);
        assertFalse(deleteStudent.isError());
        assertEquals(HttpStatus.NO_CONTENT, deleteStudent.getHttpStatusCode());
    }

    /**
     * Unhappy Path
     */
    @Test
    void createReadUpdateAndDelete_Student_Unhappy() {
        // Create Student Request Object
        var createStudentRequest = getCreateStudentRequest();
        createStudentRequest.setLastName(null);

        // Create Student
        Either<FailureResponse, SuccessResponse> createResponse = studentService.createStudent(createStudentRequest);
        assertTrue(createResponse.isLeft());
        assertEquals(HttpStatus.BAD_REQUEST, createResponse.getLeft().getApiResponse().getHttpStatusCode());

        //TODO query a non-existent student

        //TODO update a non-existent student

        //TODO delete a non existent student
    }

    @Test
    void createTwentStudents_findAllStudents_clean() {
        //students KSK/2024/0001 - KSK/2024/0020
        int pageNumber = 1; //pageNumber is 0 index based
        int pageSize = 5;
        List<String> studentIds = new ArrayList<>();
        List<CreateStudentRequest> twentyCreateStudentRequests = SudentTestUtil.getTwentyCreateStudentsRequests();

        twentyCreateStudentRequests.forEach(createStudentRequest -> {
            Either<FailureResponse, SuccessResponse> createResponse = studentService.createStudent(createStudentRequest);
            assertTrue(createResponse.isRight());
            assertEquals(HttpStatus.CREATED, createResponse.get().getApiResponse().getHttpStatusCode());
            String studentId = createResponse.get().getBody().get("studentId").asText();
            studentIds.add(studentId);
        });

        //assertions
        int firstStudentIndex = pageSize * pageNumber + 1;
        Either<FailureResponse, SuccessResponse> allStudents = studentService.findAllStudents(pageNumber, pageSize);
        assertTrue(allStudents.isRight());
        Either<FailureResponse, ArrayNode> arrayNodesEither = allStudents
                .map(successResponse -> (ArrayNode) successResponse.getBody().get("students"));
        ArrayNode arrayNodes = arrayNodesEither.get();
        List<StudentResponse> students = AppUtil.convertJsonNodeToList(arrayNodes, StudentResponse.class);
        assertEquals(pageSize, students.size());
        assertTrue(studentIds.contains(students.getFirst().getStudentId()));
        Either<FailureResponse, Long> longEither = allStudents
                .map(successResponse -> successResponse.getBody().get("totalCount").asLong());
        assertEquals(twentyCreateStudentRequests.size(), longEither.get());

        //clean up
        studentIds.forEach(stdId -> {
            ApiResponse deleteStudent = studentService.deleteStudent(stdId);
            assertFalse(deleteStudent.isError());
            assertEquals(HttpStatus.NO_CONTENT, deleteStudent.getHttpStatusCode());
        });

        //student list should be zero after clean up
        Either<FailureResponse, SuccessResponse> allEmptyStudents = studentService.findAllStudents(pageNumber, pageSize);
        assertTrue(allEmptyStudents.isRight());
        Either<FailureResponse, ArrayNode> arrayEmptyNodesEither = allEmptyStudents
                .map(successResponse -> (ArrayNode) successResponse.getBody().get("students"));
        ArrayNode arrayEmptyNodes = arrayEmptyNodesEither.get();
        List<StudentResponse> emptyStudents = AppUtil.convertJsonNodeToList(arrayEmptyNodes, StudentResponse.class);
        assertEquals(0, emptyStudents.size());
    }

    @Test
    void createStudent_findStudentAndAccount_andClean() {
        // Create Student Request Object
        var createStudentRequest = getCreateStudentRequest();

        // Create Student
        Either<FailureResponse, SuccessResponse> createResponse = studentService.createStudent(createStudentRequest);
        assertTrue(createResponse.isRight());
        var createStudentResponse = createResponse.get();
        assertEquals(HttpStatus.CREATED, createResponse.get().getApiResponse().getHttpStatusCode());
        var studentId = createStudentResponse.getBody().get("studentId").asText();

        // Read Student and Account
        Either<FailureResponse, SuccessResponse> readResponse = studentService.findStudentAndAccount(studentId);
        assertTrue(readResponse.isRight());
        var readStudentResponse = readResponse.get().getBody();
        assertEquals(HttpStatus.OK, readResponse.get().getApiResponse().getHttpStatusCode());
        assertEquals(BigDecimal.valueOf(0.00), BigDecimal.valueOf(readStudentResponse.get("accountBalance").doubleValue()));
        assertEquals(studentId, readStudentResponse.get("studentResponse").get("studentId").asText());
        assertEquals("David", readStudentResponse.get("studentResponse").get("firstName").asText());

        // Delete Student
        ApiResponse deleteStudent = studentService.deleteStudent(studentId);
        assertFalse(deleteStudent.isError());
        assertEquals(HttpStatus.NO_CONTENT, deleteStudent.getHttpStatusCode());
    }

    @Test
    void createTwentyStudents_findAllStudentAndAccounts_clean() {
        //students KSK-2024-0001 - KSK-2024-0020
        int pageNumber = 1; //pageNumber is 0 index based
        int pageSize = 5;
        List<String> studentIds = new ArrayList<>();
        List<CreateStudentRequest> twentyCreateStudentRequests = SudentTestUtil.getTwentyCreateStudentsRequests();

        twentyCreateStudentRequests.forEach(createStudentRequest -> {
            Either<FailureResponse, SuccessResponse> createResponse = studentService.createStudent(createStudentRequest);
            assertTrue(createResponse.isRight());
            assertEquals(HttpStatus.CREATED, createResponse.get().getApiResponse().getHttpStatusCode());
            String studentId = createResponse.get().getBody().get("studentId").asText();
            studentIds.add(studentId);
        });

        //assertions
        int firstStudentIndex = pageSize * pageNumber + 1;
        Either<FailureResponse, SuccessResponse> allStudents = studentService.findAllStudentAndAccounts(pageNumber, pageSize);
        assertTrue(allStudents.isRight());
        Either<FailureResponse, ArrayNode> arrayNodesEither = allStudents
                .map(successResponse -> (ArrayNode) successResponse.getBody().get("studentAccounts"));
        ArrayNode arrayNodes = arrayNodesEither.get();
        List<StudentAccountBalanceResponse> students = AppUtil.convertJsonNodeToList(arrayNodes, StudentAccountBalanceResponse.class);
        assertEquals(pageSize, students.size());
        var firstStudent = students.getFirst();
        var firstStudentResponse = firstStudent.getStudentResponse();
        assertTrue(studentIds.contains(firstStudentResponse.getStudentId()));
        assertEquals(BigDecimal.valueOf(0.00), BigDecimal.valueOf(firstStudent.getAccountBalance().doubleValue()));
        Either<FailureResponse, Long> longEither = allStudents
                .map(successResponse -> successResponse.getBody().get("totalCount").asLong());
        assertEquals(twentyCreateStudentRequests.size(), longEither.get());

        //clean up
        studentIds.forEach(stdId -> {
            ApiResponse deleteStudent = studentService.deleteStudent(stdId);
            assertFalse(deleteStudent.isError());
            assertEquals(HttpStatus.NO_CONTENT, deleteStudent.getHttpStatusCode());
        });

        //student list should be zero after clean up
        Either<FailureResponse, SuccessResponse> allEmptyStudents = studentService.findAllStudentAndAccounts(pageNumber, pageSize);
        assertTrue(allEmptyStudents.isRight());
        Either<FailureResponse, ArrayNode> arrayEmptyNodesEither = allEmptyStudents
                .map(successResponse -> (ArrayNode) successResponse.getBody().get("studentAccounts"));
        ArrayNode arrayEmptyNodes = arrayEmptyNodesEither.get();
        List<StudentAccountBalanceResponse> emptyStudents = AppUtil.convertJsonNodeToList(arrayEmptyNodes, StudentAccountBalanceResponse.class);
        assertEquals(0, emptyStudents.size());
    }

    private UpdateStudentRequest getUpdateStudentRequest() {
        return SudentTestUtil.getUpdateStudentRequest();
    }

    private CreateStudentRequest getCreateStudentRequest() {
        return SudentTestUtil.getCreateStudentRequest();
    }
}