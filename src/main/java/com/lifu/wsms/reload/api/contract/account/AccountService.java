package com.lifu.wsms.reload.api.contract.account;

import com.lifu.wsms.reload.dto.response.FailureResponse;
import com.lifu.wsms.reload.dto.response.SuccessResponse;
import io.vavr.control.Either;

/**
 * Service interface for managing student accounts.
 */
public interface AccountService {

    /**
     * Retrieves the details of a specific student and their account based on the provided student ID.
     *
     * @param studentId The ID of the student whose details are to be retrieved.
     * @return Either a FailureResponse if there is an error, or a SuccessResponse containing the student
     *         and account details if the operation is successful.
     */
    Either<FailureResponse, SuccessResponse> findStudentAndAccount(String studentId);

    /**
     * Retrieves a paginated list of student-account pairs.
     *
     * @param pageNumber The page number of the results (0-indexed).
     * @param pageSize   The maximum number of items per page.
     * @return Either a FailureResponse if there is an error, or a SuccessResponse containing a paginated
     *         list of student-account pairs if the operation is successful.
     */
    Either<FailureResponse, SuccessResponse> findAllStudentAndAccounts(int pageNumber, int pageSize);
}

