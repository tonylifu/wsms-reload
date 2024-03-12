package com.lifu.wsms.reload.dto.response.student;

import com.lifu.wsms.reload.dto.Address;
import com.lifu.wsms.reload.dto.request.student.CreateStudentRequest;
import com.lifu.wsms.reload.enums.Gender;
import lombok.Builder;

public class StudentResponse extends CreateStudentRequest {
    @Builder
    public StudentResponse(String studentId,
                           String firstName,
                           String middleName,
                           String lastName,
                           long dob,
                           Gender gender,
                           Address address) {
        super(studentId, firstName, middleName, lastName, dob, gender, address);
    }
}
