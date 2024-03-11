package com.lifu.wsms.reload.dto.request;

import com.lifu.wsms.reload.enums.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStudentRequest extends CreateStudentRequest {

    @Builder
    public UpdateStudentRequest(String studentId,
                                String firstName,
                                String middleName,
                                String lastName,
                                long dob,
                                Gender gender,
                                Address address) {
        super(studentId, firstName, middleName, lastName, dob, gender, address);
    }
}
