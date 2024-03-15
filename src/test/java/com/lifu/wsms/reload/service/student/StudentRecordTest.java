package com.lifu.wsms.reload.service.student;

import com.lifu.wsms.reload.api.StudentService;
import com.lifu.wsms.reload.dto.request.student.CreateStudentRequest;
import com.lifu.wsms.reload.dto.request.student.UpdateStudentRequest;
import com.lifu.wsms.reload.dto.response.ApiResponse;
import com.lifu.wsms.reload.dto.response.FailureResponse;
import com.lifu.wsms.reload.dto.response.SuccessResponse;
import com.lifu.wsms.reload.util.TestUtil;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentRecordTest {
    @Autowired
    private StudentService studentService;

    /**
     * Happy path
     */
    @Test
    void createReadUpdateAndDelete_Student() {
        // Create Student Request Object
        var createStudentRequest = getCreateStudentRequest();

        // Create Student
        Either<FailureResponse, SuccessResponse> createResponse = studentService.createStudent(createStudentRequest);
        assertTrue(createResponse.isRight());
        var createStudentResponse = createResponse.get();
        assertEquals(HttpStatus.CREATED, createResponse.get().getApiResponse().getHttpStatusCode());
        var student = createStudentResponse.getBody();
        assertEquals("KSK/2024/1234", student.get("studentId").asText());

        // Read Student
        Either<FailureResponse, SuccessResponse> readResponse = studentService.findStudent("KSK/2024/1234");
        assertTrue(readResponse.isRight());
        var readStudentResponse = readResponse.get().getBody();
        assertEquals(HttpStatus.OK, readResponse.get().getApiResponse().getHttpStatusCode());
        assertEquals("KSK/2024/1234", readStudentResponse.get("studentId").asText());
        assertEquals("David", readStudentResponse.get("firstName").asText());

        // Update Student
        var updateStudentRequest = getUpdateStudentRequest();
        Either<FailureResponse, SuccessResponse> updateResponse = studentService.updateStudent(updateStudentRequest);
        assertFalse(updateResponse.isLeft());
        var updateResult = updateResponse.get().getBody();
        assertEquals("KSK/2024/1234", updateResult.get("studentId").asText());
        assertEquals("Joan", updateResult.get("firstName").asText());

        // Delete Student
        ApiResponse deleteStudent = studentService.deleteStudent("KSK/2024/1234");
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
        createStudentRequest.setStudentId(null);

        // Create Student
        Either<FailureResponse, SuccessResponse> createResponse = studentService.createStudent(createStudentRequest);
        assertTrue(createResponse.isLeft());
        assertEquals(HttpStatus.BAD_REQUEST, createResponse.getLeft().getApiResponse().getHttpStatusCode());

        //TODO query a non-existent student

        //TODO update a non-existent student

        //TODO delete a non existent student
    }

//    @Test
//    void findStudent() {
//        Either<FailureResponse, SuccessResponse> readResponse = studentService.findStudent("KSK/2024/1234");
//        assertTrue(readResponse.isRight());
//        var readStudentResponse = readResponse.get().getBody();
//        assertEquals(HttpStatus.OK, readResponse.get().getApiResponse().getHttpStatusCode());
//        assertEquals("KSK/2024/1234", readStudentResponse.get("studentId").asText());
//        assertEquals("David", readStudentResponse.get("firstName").asText());
//    }

    @Test
    void updateStudent() {
    }

    @Test
    void deleteStudent() {
        ApiResponse deleteStudent = studentService.deleteStudent("KSK/2024/1234");
        System.out.println(deleteStudent.getResponseMessage());
        assertFalse(deleteStudent.isError());
        assertEquals(HttpStatus.NO_CONTENT, deleteStudent.getHttpStatusCode());
    }

    private UpdateStudentRequest getUpdateStudentRequest() {
        return TestUtil.getUpdateStudentRequest();
    }

    private CreateStudentRequest getCreateStudentRequest() {
        return TestUtil.getCreateStudentRequest();
    }
}