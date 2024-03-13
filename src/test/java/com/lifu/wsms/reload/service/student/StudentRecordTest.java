package com.lifu.wsms.reload.service.student;

import com.lifu.wsms.reload.api.AppUtil;
import com.lifu.wsms.reload.api.StudentService;
import com.lifu.wsms.reload.dto.Address;
import com.lifu.wsms.reload.dto.Contact;
import com.lifu.wsms.reload.dto.LegalGuardian;
import com.lifu.wsms.reload.dto.request.student.CreateStudentRequest;
import com.lifu.wsms.reload.dto.request.student.UpdateStudentRequest;
import com.lifu.wsms.reload.dto.response.ApiResponse;
import com.lifu.wsms.reload.dto.response.FailureResponse;
import com.lifu.wsms.reload.dto.response.SuccessResponse;
import com.lifu.wsms.reload.enums.Gender;
import com.lifu.wsms.reload.util.TestUtil;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

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
        var createResultMap = createResponse.get().getBody();
        assertEquals(HttpStatus.CREATED, createResponse.get().getApiResponse().getHttpStatusCode());
        assertEquals("AABB2024", createResultMap.get("studentId"));

        // Read Student
        Either<FailureResponse, SuccessResponse> readResponse = studentService.findStudent("AABB2024");
        assertTrue(readResponse.isRight());
        var readResultMap = readResponse.get().getBody();
        assertEquals(HttpStatus.OK, readResponse.get().getApiResponse().getHttpStatusCode());
        assertEquals("AABB2024", readResultMap.get("studentId"));
        assertEquals("David", readResultMap.get("firstName"));

        // Update Student
        var updateStudentRequest = getUpdateStudentRequest();
        Either<FailureResponse, SuccessResponse> updateResponse = studentService.updateStudent(updateStudentRequest);
        assertFalse(updateResponse.isLeft());
        var updateResultMap = updateResponse.get().getBody();
        assertEquals("AABB2024", updateResultMap.get("studentId"));
        assertEquals("Joan", readResultMap.get("firstName"));

        // Delete Student
        ApiResponse deleteStudent = studentService.deleteStudent("AABB2024");
        assertFalse(deleteStudent.isError());
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
        var createResultMap = createResponse.get().getBody();
        assertEquals(HttpStatus.BAD_REQUEST, createResponse.get().getApiResponse().getHttpStatusCode());
        assertNull(createResultMap.get("studentId"));
    }

    private UpdateStudentRequest getUpdateStudentRequest() {
        return UpdateStudentRequest.builder()
                .studentId("AABB2024")
                .firstName("Joan")
                .middleName("Owogbuo")
                .lastName("Lifu")
                .dob(AppUtil.convertLocalDateToLong(LocalDate.of(2011,6,13)))
                .gender(Gender.FEMALE)
                .address(Address.builder()
                        .houseNumber("21")
                        .streetName("Lyon Crescent")
                        .area("Stirling")
                        .country("United Kingdom")
                        .build())
                .contact(Contact.builder()
                        .email("joanlifu@gmail.com")
                        .mobilePhone("+447766433489")
                        .telephone("013240000000")
                        .build())
                .legalGuardian(LegalGuardian.builder()
                        .isBiologicalParentListed(Boolean.TRUE)
                        .mother("Ladi")
                        .motherContactInformation(Contact.builder()
                                .email("ladi@gmail.com")
                                .mobilePhone("+447766433489")
                                .telephone("013240000000")
                                .build())
                        .father("Ohiero")
                        .fatherContactInformation(Contact.builder()
                                .email("ohiero@gmail.com")
                                .mobilePhone("+447766433489")
                                .telephone("013240000000")
                                .build())
                        .build())
                .build();
    }

    private CreateStudentRequest getCreateStudentRequest() {
        return TestUtil.getCreateStudentRequest();
    }

}