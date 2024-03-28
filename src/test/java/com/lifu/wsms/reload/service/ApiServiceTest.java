package com.lifu.wsms.reload.service;

import com.lifu.wsms.reload.dto.response.finance.StudentAccountBalanceResponse;
import com.lifu.wsms.reload.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ApiServiceTest {
    @Test
    void validateCreateStudent_happy() {
        var createStudent = TestUtil.getCreateStudentRequest();
        var result = ApiService.validateCreateStudent(createStudent);
        System.out.println(result);
        assertTrue(result.isRight());
        assertTrue(result.get());
    }

    @Test
    void validateCreateStudent_sad() {
        var createStudent = TestUtil.getCreateStudentRequest();
        createStudent.setLastName(null);
        var result = ApiService.validateCreateStudent(createStudent);
        assertTrue(result.isLeft());
        var failureResponse = result.getLeft();
        assertEquals(HttpStatus.BAD_REQUEST, failureResponse.getApiResponse().getHttpStatusCode());
    }

    @Test
    void validateUpdateStudent_happy() {
        var updateStudent = TestUtil.getUpdateStudentRequest();
        var result = ApiService.validateUpdateStudent(updateStudent);
        assertTrue(result.isRight());
        assertTrue(result.get());
    }

    @Test
    void validateUpdateStudent_sad() {
        var updateStudent = TestUtil.getUpdateStudentRequest();
        updateStudent.setStudentId(null);
        var result = ApiService.validateUpdateStudent(updateStudent);
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
        var result = ApiService.populateStudentForUpdate(student, updateStudent);
        assertNotNull(result.getFirstName());
        assertEquals(updateStudent.getFirstName(), result.getFirstName());
    }

    @Test
    void getStudentAccountBalanceResponseFromObjects() {
        var student = TestUtil.getStudent();
        BigDecimal balance = BigDecimal.valueOf(4.55);
        Object[] objects = {student, balance};
        StudentAccountBalanceResponse studentAccountObject = ApiService.getStudentAccountBalanceResponseFromObjects(objects);
        String returnedStudentId = studentAccountObject.getStudentResponse().getStudentId();
        BigDecimal returnedBalance = studentAccountObject.getAccountBalance();
        assertEquals(student.getStudentId(), returnedStudentId);
        assertEquals(balance, returnedBalance);
    }

    @Test
    void getStudentAccountBalanceResponseFromObjectList() {
        var student1 = TestUtil.getStudent();
        var student2 = TestUtil.getStudent();
        BigDecimal balance1 = BigDecimal.valueOf(4.55);
        BigDecimal balance2 = BigDecimal.valueOf(9.99);

        List<Object[]> objectList = new ArrayList<>();
        objectList.add(new Object[]{student1, balance1});
        objectList.add(new Object[]{student2, balance2});

        PageRequest pageRequest = PageRequest.of(0, objectList.size());
        PageImpl<Object[]> objects = new PageImpl<>(objectList, pageRequest, objectList.size());

        List<StudentAccountBalanceResponse> studentAccountObjects = ApiService.getStudentAccountBalanceResponseFromObjectList(objects);

        var firstStudentResponse = studentAccountObjects.getFirst();
        var lastStudentResponse = studentAccountObjects.getLast();

        assertEquals(2, studentAccountObjects.size());
        assertEquals(student1.getStudentId(), firstStudentResponse.getStudentResponse().getStudentId());
        assertEquals(balance1, firstStudentResponse.getAccountBalance());
        assertEquals(student2.getStudentId(), lastStudentResponse.getStudentResponse().getStudentId());
        assertEquals(balance2, lastStudentResponse.getAccountBalance());
    }
}