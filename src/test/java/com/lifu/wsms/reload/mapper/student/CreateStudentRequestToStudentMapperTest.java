package com.lifu.wsms.reload.mapper.student;

import com.lifu.wsms.reload.util.SudentTestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CreateStudentRequestToStudentMapperTest {

    @Test
    void toStudent() {
        var createStudent = SudentTestUtil.getCreateStudentRequest();
        var student = CreateStudentRequestToStudentMapper.INSTANCE.toStudent(createStudent);
        assertEquals(createStudent.getFirstName(), student.getFirstName());
        assertEquals(createStudent.getAddress().getHouseNumber(), student.getAddress().getHouseNumber());
    }
}