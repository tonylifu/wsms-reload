package com.lifu.wsms.reload.mapper;

import com.lifu.wsms.reload.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentToStudentResponseMapperTest {

    @Test
    void toStudentResponse() {
        var student = TestUtil.getStudent();
        var studentResponse = StudentToStudentResponseMapper.INSTANCE.toStudentResponse(student);
        assertEquals(student.getStudentId(), studentResponse.getStudentId());
        assertEquals(student.getAddress().getHouseNumber(), studentResponse.getAddress().getHouseNumber());

    }
}