package com.lifu.wsms.reload.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static com.lifu.wsms.reload.controller.student.StudentController.STUDENT_PATH;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class RestExceptionHandlerTest {
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleHttpMessageNotReadableException() throws Exception {
        var request = "{\n" +
                "    \"studentId\": \"KSK-2024-1234\",\n" +
                "    \"firstName\": \"David\",\n" +
                "    \"middleName\": \"Owogoga\",\n" +
                "    \"lastName\": \"Lifu\",\n" +
                "    \"dob\": \"2010-01-01\",\n" +
                "    \"gender\": \"male lion\",\n" +
                "    \"address\": {\n" +
                "        \"houseNumber\": \"21\",\n" +
                "        \"streetName\": \"Lyon Crescent\",\n" +
                "        \"area\": \"Stirling\",\n" +
                "        \"localGovtArea\": \"Stirling\",\n" +
                "        \"state\": \"Scotland\",\n" +
                "        \"country\": \"United Kingdom\"\n" +
                "    },\n" +
                "    \"contact\": {\n" +
                "        \"email\": \"davidlifu@gmail.com\",\n" +
                "        \"mobilePhone\": \"07766433489\",\n" +
                "        \"telephone\": \"\"\n" +
                "    },\n" +
                "    \"legalGuardian\": {\n" +
                "        \"isBiologicalParentListed\": true,\n" +
                "        \"father\": \"Anthony Lifu\",\n" +
                "        \"fatherContactInformation\": {\n" +
                "            \"email\": \"tlifu75@gmail.com\",\n" +
                "            \"mobilePhone\": \"07766433489\",\n" +
                "            \"telephone\": \"\"\n" +
                "        },\n" +
                "        \"mother\": \"\",\n" +
                "        \"motherContactInformation\": {\n" +
                "            \"email\": \"ladilifu@gmail.com\",\n" +
                "            \"mobilePhone\": \"07766433489\",\n" +
                "            \"telephone\": \"\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"currentGrade\": \"SSS120241\",\n" +
                "    \"isDisabled\": false\n" +
                "}";

        mockMvc.perform(
                        post(STUDENT_PATH)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid JSON request body")));
    }
}