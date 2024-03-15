package com.lifu.wsms.reload.service.student;

import com.lifu.wsms.reload.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentRecordServiceTest {
    @Test
    void validateCreateStudent_happy() {
        var createStudent = TestUtil.getCreateStudentRequest();
        var result = StudentRecordService.validateCreateStudent(createStudent);
        System.out.println(result);
        assertTrue(result.isRight());
        assertTrue(result.get());
    }

    @Test
    void validateCreateStudent_sad() {
        var createStudent = TestUtil.getCreateStudentRequest();
        createStudent.setStudentId(null);
        var result = StudentRecordService.validateCreateStudent(createStudent);
        assertTrue(result.isLeft());
        var failureResponse = result.getLeft();
        assertEquals(HttpStatus.BAD_REQUEST, failureResponse.getApiResponse().getHttpStatusCode());
    }

    @Test
    void validateUpdateStudent_happy() {
        var updateStudent = TestUtil.getUpdateStudentRequest();
        var result = StudentRecordService.validateUpdateStudent(updateStudent);
        assertTrue(result.isRight());
        assertTrue(result.get());
    }

    @Test
    void validateUpdateStudent_sad() {
        var updateStudent = TestUtil.getUpdateStudentRequest();
        updateStudent.setStudentId(null);
        var result = StudentRecordService.validateUpdateStudent(updateStudent);
        assertTrue(result.isLeft());
        var failureResponse = result.getLeft();
        assertEquals(HttpStatus.BAD_REQUEST, failureResponse.getApiResponse().getHttpStatusCode());
    }

    @Test
    void populateStudentForUpdate() {
        var updateStudent = TestUtil.getUpdateStudentRequest();
        var student = TestUtil.getStudent();
        student.setFirstName(null);
        assertNull(student.getFirstName());
        assertNotNull(updateStudent.getFirstName());
        var result = StudentRecordService.populateStudentForUpdate(student, updateStudent);
        assertNotNull(result.getFirstName());
        assertEquals(updateStudent.getFirstName(), result.getFirstName());
    }
}