package com.lifu.wsms.reload.service;

import com.lifu.wsms.reload.api.StudentService;
import com.lifu.wsms.reload.dto.request.CreateStudentRequest;
import com.lifu.wsms.reload.dto.request.UpdateStudentRequest;
import com.lifu.wsms.reload.dto.response.ApiResponse;
import com.lifu.wsms.reload.dto.response.FailureResponse;
import com.lifu.wsms.reload.dto.response.SuccessResponse;
import io.vavr.control.Either;

public class StudentRecord implements StudentService {
    @Override
    public Either<FailureResponse, SuccessResponse> createStudent(CreateStudentRequest createStudentRequest) {
        return null;
    }

    @Override
    public Either<FailureResponse, SuccessResponse> findStudent(String studentId) {
        return null;
    }

    @Override
    public Either<FailureResponse, SuccessResponse> updateStudent(UpdateStudentRequest updateStudentRequest) {
        return null;
    }

    @Override
    public ApiResponse deleteStudent(String studentId) {
        return null;
    }
}
