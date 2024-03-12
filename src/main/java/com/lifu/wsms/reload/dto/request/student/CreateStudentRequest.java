package com.lifu.wsms.reload.dto.request.student;

import com.lifu.wsms.reload.dto.request.Address;
import com.lifu.wsms.reload.enums.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateStudentRequest extends StudentRequest {
    private String firstName;
    private String middleName;
    private String lastName;
    private long dob;
    private Gender gender;
    private Address address;

    @Builder
    public CreateStudentRequest(String studentId,
                                String firstName,
                                String middleName,
                                String lastName,
                                long dob,
                                Gender gender,
                                Address address) {
        super(studentId);
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.address = address;
    }
}
