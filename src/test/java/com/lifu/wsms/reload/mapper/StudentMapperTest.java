package com.lifu.wsms.reload.mapper;

import com.lifu.wsms.reload.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentMapperTest {

    @Test
    void toStudent() {
        var createStudent = TestUtil.getCreateStudentRequest();
        var student = CreateStudentRequestToStudentMapper.INSTANCE.toStudent(createStudent);
        assertEquals(createStudent.getStudentId(), student.getStudentId());
        assertEquals(createStudent.getFirstName(), student.getFirstName());
        assertEquals(createStudent.getAddress().getHouseNumber(), student.getAddress().getHouseNumber());
    }
}