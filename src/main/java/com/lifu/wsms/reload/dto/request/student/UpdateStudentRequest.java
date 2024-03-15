package com.lifu.wsms.reload.dto.request.student;

import com.lifu.wsms.reload.dto.Address;
import com.lifu.wsms.reload.dto.Contact;
import com.lifu.wsms.reload.dto.LegalGuardian;
import com.lifu.wsms.reload.enums.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Builder
public class UpdateStudentRequest {
    private String studentId;
    private String firstName;
    private String middleName;
    private String lastName;
    private long dob;
    private Gender gender;
    private Address address;
    private Contact contact;
    private LegalGuardian legalGuardian;
    private String currentGrade;
    private boolean isDisabled;
    private String disabilityDetails;
}
