package com.lifu.wsms.reload.dto.request.student;

import com.lifu.wsms.reload.dto.Address;
import com.lifu.wsms.reload.dto.Contact;
import com.lifu.wsms.reload.dto.LegalGuardian;
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
                                Address address,
                                Contact contact,
                                LegalGuardian legalGuardian) {
        super(studentId, firstName, middleName, lastName, dob, gender, address, contact, legalGuardian);
    }
}
}
