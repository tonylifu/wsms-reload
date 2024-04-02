package com.lifu.wsms.reload.controller.student;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifu.wsms.reload.dto.request.student.CreateStudentRequest;
import com.lifu.wsms.reload.dto.request.student.UpdateStudentRequest;
import com.lifu.wsms.reload.enums.Gender;
import com.lifu.wsms.reload.util.SudentTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static com.lifu.wsms.reload.controller.student.StudentController.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DirtiesContext
    void createAndReadAndUpdateAndDeleteStudent() throws Exception {
        //Given => createstudentRequest object
        CreateStudentRequest createStudentRequest = SudentTestUtil.getCreateStudentRequest();

        //When => create student and return location in the header
        String location = mockMvc.perform(
                post(STUDENT_PATH)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createStudentRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"))
                .andReturn().getResponse().getHeader("location");

        //When => Read the student by studentId
        String jsonResponse = this.mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JsonNode jsonNodeResponse = objectMapper.readTree(jsonResponse);
        JsonNode jsonNodeResponseBody = jsonNodeResponse.get("body");

        //Then
        String studentId = jsonNodeResponseBody.get("studentId").asText();
        assertEquals("David", jsonNodeResponseBody.get("firstName").asText());
        assertEquals("Lifu", jsonNodeResponseBody.get("lastName").asText());
        assertEquals(Gender.MALE.name(), jsonNodeResponseBody.get("gender").asText());

        //Given => an updateStudentRequest with updated fields and studentId
        UpdateStudentRequest updateStudentRequest = objectMapper
                .treeToValue(jsonNodeResponseBody, UpdateStudentRequest.class);
        updateStudentRequest.setFirstName("Joan");
        updateStudentRequest.setLastName("Ohiero-Lifu");
        updateStudentRequest.setGender(Gender.FEMALE);

        //When => you update and return location
        String updateLocation = this.mockMvc.perform(put(STUDENT_PATH)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateStudentRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists("location"))
                .andReturn().getResponse().getHeader("location");

        //Then => Read student by studentId after update
        String jsonResponseUpdated = this.mockMvc.perform(get(updateLocation))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.studentId").value(studentId))
                .andReturn().getResponse().getContentAsString();
        JsonNode jsonNodeResponseUpdated = objectMapper.readTree(jsonResponseUpdated);
        JsonNode jsonNodeResponseBodyUpdated = jsonNodeResponseUpdated.get("body");

        //Then => verify the update
        assertEquals(studentId, jsonNodeResponseBodyUpdated.get("studentId").asText());
        assertEquals("Joan", jsonNodeResponseBodyUpdated.get("firstName").asText());
        assertEquals("Ohiero-Lifu", jsonNodeResponseBodyUpdated.get("lastName").asText());
        assertEquals(Gender.FEMALE.name(), jsonNodeResponseBodyUpdated.get("gender").asText());

        //Then => assert student balance
        this.mockMvc.perform(get(STUDENT_ACCOUNT_PATH_ID, studentId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.accountBalance").value(0))
                .andExpect(jsonPath("$.body.studentResponse.studentId").value(studentId));

        //Then => delete by studentId and assert deletion to clean up
        this.mockMvc.perform(delete(updateLocation))
                .andExpect(status().isNoContent());
    }


    @Test
    void findAllStudents() throws Exception {
        mockMvc.perform(get(STUDENT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.body.totalCount").value(25))
                .andExpect(jsonPath("$.apiResponse.responseMessage").value("Successful"))
                .andExpect(jsonPath("$.apiResponse.responseCode").value("003"))
                .andExpect(jsonPath("$.apiResponse.httpStatusCode").value("OK"))
                .andExpect(jsonPath("$.apiResponse.error").value(false));
    }

    @Test
    void findAllStudentsAndAccountBalances() throws Exception {
        mockMvc.perform(get(STUDENT_ACCOUNT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.body.totalCount").value(25))
                .andExpect(jsonPath("$.apiResponse.responseMessage").value("Successful"))
                .andExpect(jsonPath("$.apiResponse.responseCode").value("003"))
                .andExpect(jsonPath("$.apiResponse.httpStatusCode").value("OK"))
                .andExpect(jsonPath("$.apiResponse.error").value(false));
    }
}