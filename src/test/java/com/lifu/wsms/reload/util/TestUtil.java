package com.lifu.wsms.reload.util;

import com.lifu.wsms.reload.api.AppUtil;
import com.lifu.wsms.reload.dto.Address;
import com.lifu.wsms.reload.dto.Contact;
import com.lifu.wsms.reload.dto.LegalGuardian;
import com.lifu.wsms.reload.dto.request.student.CreateStudentRequest;
import com.lifu.wsms.reload.entity.Student;
import com.lifu.wsms.reload.enums.Gender;
import com.lifu.wsms.reload.mapper.StudentMapper;

import java.time.LocalDate;

public class TestUtil {
    private TestUtil() {}

    public static CreateStudentRequest getCreateStudentRequest() {
        return CreateStudentRequest.builder()
                .studentId("AABB2024")
                .firstName("David")
                .middleName("Owogoga")
                .lastName("Lifu")
                .dob(AppUtil.convertLocalDateToLong(LocalDate.of(2010,1,1)))
                .gender(Gender.MALE)
                .address(Address.builder()
                        .houseNumber("21")
                        .streetName("Lyon Crescent")
                        .area("Stirling")
                        .country("United Kingdom")
                        .build())
                .contact(Contact.builder()
                        .email("davidlifu@gmail.com")
                        .mobilePhone("+447766433489")
                        .telephone("013240000000")
                        .build())
                .legalGuardian(LegalGuardian.builder()
                        .isBiologicalParentListed(true)
                        .mother("Ladi")
                        .motherContactInformation(Contact.builder()
                                .email("ladi@gmail.com")
                                .mobilePhone("+447766433489")
                                .telephone("013240000000")
                                .build())
                        .father("Ohiero")
                        .fatherContactInformation(Contact.builder()
                                .email("ohiero@gmail.com")
                                .mobilePhone("+447766433489")
                                .telephone("013240000000")
                                .build())
                        .build())
                .build();
    }

    public static Student getStudent() {
        return StudentMapper.INSTANCE.toStudent(getCreateStudentRequest());
    }
}
