package com.lifu.wsms.reload.api.contract.student;

import com.lifu.wsms.reload.api.contract.account.AccountService;
import com.lifu.wsms.reload.dto.request.student.CreateStudentRequest;
import com.lifu.wsms.reload.dto.request.student.UpdateStudentRequest;
import com.lifu.wsms.reload.dto.response.ApiResponse;
import com.lifu.wsms.reload.dto.response.FailureResponse;
import com.lifu.wsms.reload.dto.response.SuccessResponse;
import io.vavr.control.Either;

/**
 * This interface defines all interactions with the API related to the Student model.
 */
public interface StudentService extends AccountService {

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

    /**
     * Retrieves a paginated list of all students from the database.
     *
     * @param pageNumber The page number of the results to retrieve.
     * @param pageSize   The number of students per page.
     * @return An {@code Either} representing the result of the operation. If the operation is successful,
     *         returns a {@code SuccessResponse} containing the paginated list of students. If an error occurs
     *         during the operation, returns a {@code FailureResponse} containing information about the error.
     *         The failure response may include an error code, error message, and HTTP status code.
     */
    Either<FailureResponse, SuccessResponse> findAllStudents(int pageNumber, int pageSize);
}
