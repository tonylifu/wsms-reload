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
@Builder
public class CreateStudentRequest {
    private String studentId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String dob;
    private Gender gender;
    private Address address;
    private Contact contact;
    private LegalGuardian legalGuardian;
    private String currentGrade;
    private boolean isDisabled;
}
