package com.lifu.wsms.reload.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.lifu.wsms.reload.dto.response.student.StudentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppUtilTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void convertLocalDateToLong() {
        LocalDate localDate = LocalDate.of(2024, 1, 1);
        long longDate = 1704067200000L;
        assertEquals(longDate, AppUtil.convertLocalDateToLong(localDate));
        assertEquals(localDate.toString(), AppUtil.convertLongToLocalDate(longDate).toString());

    }

    @Test
    void isValidStudentId() {
        String validStudentId = AppUtil.generateStudentId();
        String inValidStudentId = "KSK/2024/20002020";
        assertTrue(AppUtil.isValidStudentId(validStudentId));
        assertFalse(AppUtil.isValidStudentId(inValidStudentId));
    }

    @Test
    void convertJsonNodeToList_And_convertListToJsonNode() {
        ArrayNode jsonNode = JsonNodeFactory.instance.arrayNode();
        jsonNode.add(objectMapper.valueToTree(StudentResponse.builder()
                        .firstName("David")
                        .lastName("Lifu")
                .build()));

        jsonNode.add(objectMapper.valueToTree(StudentResponse.builder()
                .firstName("Joan")
                .lastName("Lifu")
                .build()));

        jsonNode.add(objectMapper.valueToTree(StudentResponse.builder()
                .firstName("Pio")
                .lastName("Lifu")
                .build()));

        jsonNode.add(objectMapper.valueToTree(StudentResponse.builder()
                .firstName("Tadeo")
                .lastName("Lifu")
                .build()));

        List<StudentResponse> students = AppUtil.convertJsonNodeToList(jsonNode, StudentResponse.class);
        assertEquals(4, students.size());
        students.forEach(student -> assertEquals("Lifu", student.getLastName()));

        //List back to JsonNode
        ArrayNode studentsJsonNode = (ArrayNode) AppUtil.convertListToJsonNode(students);
        assertEquals(4, studentsJsonNode.size());
        assertEquals(jsonNode.size(), studentsJsonNode.size());
        assertEquals(jsonNode.get(0), studentsJsonNode.get(0));
        assertEquals(jsonNode.get(1), studentsJsonNode.get(1));
        assertEquals(jsonNode.get(2), studentsJsonNode.get(2));
        assertEquals(jsonNode.get(3), studentsJsonNode.get(3));
    }

}