package com.lifu.wsms.reload.exception;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static com.lifu.wsms.reload.controller.student.StudentController.STUDENT_PATH;
import static com.lifu.wsms.reload.util.TestUtil.getCreateStudentRequestJsonString;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        var request = getCreateStudentRequestJsonString();

        mockMvc.perform(
                        post(STUDENT_PATH)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid JSON request body")))
                .andExpect(jsonPath("$.apiResponse.responseCode").value("407"))
                .andExpect(jsonPath("$.apiResponse.responseMessage").value("Invalid JSON request body: [gender, studentStatus]"))
                .andExpect(jsonPath("$.apiResponse.httpStatusCode").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.apiResponse.error").value(true));
    }

}