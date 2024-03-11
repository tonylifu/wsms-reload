package com.lifu.wsms.reload.api;

import com.lifu.wsms.reload.dto.request.CreateStudentRequest;
import com.lifu.wsms.reload.dto.request.UpdateStudentRequest;
import com.lifu.wsms.reload.dto.response.ApiResponse;
import com.lifu.wsms.reload.dto.response.FailureResponse;
import com.lifu.wsms.reload.dto.response.SuccessResponse;
import io.vavr.control.Either;

/**
 * This interface defines all interactions with the API related to the Student model.
 */
public interface StudentService {

    /**
     * Creates a new student.
     *
     * @param createStudentRequest The request object containing information to create the student.
     * @return Either a failure response or a success response.
     */
    Either<FailureResponse, SuccessResponse> createStudent(CreateStudentRequest createStudentRequest);

    /**
     * Finds a student by ID.
     *
     * @param studentId The ID of the student to find.
     * @return Either a failure response or a success response.
     */
    Either<FailureResponse, SuccessResponse> findStudent(String studentId);

    /**
     * Updates an existing student.
     *
     * @param updateStudentRequest The request object containing information to update the student.
     * @return Either a failure response or a success response.
     */
    Either<FailureResponse, SuccessResponse> updateStudent(UpdateStudentRequest updateStudentRequest);

    /**
     * Deletes a student by ID.
     *
     * @param studentId The ID of the student to delete.
     * @return An API response indicating the result of the deletion.
     */
    ApiResponse deleteStudent(String studentId);
}