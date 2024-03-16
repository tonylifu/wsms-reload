package com.lifu.wsms.reload.util;

import com.lifu.wsms.reload.api.AppUtil;
import com.lifu.wsms.reload.dto.Address;
import com.lifu.wsms.reload.dto.Contact;
import com.lifu.wsms.reload.dto.LegalGuardian;
import com.lifu.wsms.reload.dto.request.student.CreateStudentRequest;
import com.lifu.wsms.reload.dto.request.student.UpdateStudentRequest;
import com.lifu.wsms.reload.entity.student.Student;
import com.lifu.wsms.reload.enums.Gender;
import com.lifu.wsms.reload.mapper.StudentMapper;
import org.apache.catalina.LifecycleState;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestUtil {
    private TestUtil() {}

    /**
     * Generates a {@link CreateStudentRequest} object with predefined values.
     *
     * @return A {@link CreateStudentRequest} object populated with predefined values.
     */
    public static CreateStudentRequest getCreateStudentRequest() {
        return CreateStudentRequest.builder()
                .studentId("KSK/2024/1234")
                .firstName("David")
                .middleName("Owogoga")
                .lastName("Lifu")
                .dob(AppUtil.convertLocalDateToLong(LocalDate.of(2010,1,1)))
                .gender(Gender.MALE)
                .currentGrade("SSS1/2024/1")
                .isDisabled(false)
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

    /**
     * Generates a {@link Student} object by mapping the values from a {@link CreateStudentRequest} object using the {@link StudentMapper}.
     *
     * @return A {@link Student} object populated with values from a predefined {@link CreateStudentRequest} object.
     */
    public static Student getStudent() {
        return StudentMapper.INSTANCE.toStudent(getCreateStudentRequest());
    }

    /**
     * Generates an {@link UpdateStudentRequest} object with predefined values for testing purposes.
     *
     * @return An {@link UpdateStudentRequest} object with predefined values.
     */
    public static UpdateStudentRequest getUpdateStudentRequest() {
        return UpdateStudentRequest.builder()
                .studentId("KSK/2024/1234")
                .firstName("Joan")
                .middleName("Owogbuo")
                .lastName("Lifu")
                .dob(AppUtil.convertLocalDateToLong(LocalDate.of(2011,6,13)))
                .gender(Gender.FEMALE)
                .currentGrade("SSS1/2024/2")
                .isDisabled(false)
                .address(Address.builder()
                        .houseNumber("21")
                        .streetName("Lyon Crescent")
                        .area("Stirling")
                        .country("United Kingdom")
                        .build())
                .contact(Contact.builder()
                        .email("joanlifu@gmail.com")
                        .mobilePhone("+447766433489")
                        .telephone("013240000000")
                        .build())
                .legalGuardian(LegalGuardian.builder()
                        .isBiologicalParentListed(Boolean.TRUE)
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

    /**
     * Generates a list of twenty {@link CreateStudentRequest} objects with student IDs ranging from 'KSK/2024/0001' to 'KSK/2024/0020'.
     *
     * @return A list of {@link CreateStudentRequest} objects containing twenty student requests.
     */
    public static List<CreateStudentRequest> getTwentyCreateStudentsRequests() {
        List<CreateStudentRequest> twentyCreateStudentRequests = new ArrayList<>();
        //students KSK/2024/0001 - KSK/2024/0020
        var prefix = "KSK/2024/";
        for (int i = 1; i <= 20; i++) {
            String studentId = prefix + String.format("%04d", i);
            var createStudentRequest = getCreateStudentRequest();
            createStudentRequest.setStudentId(studentId);
            twentyCreateStudentRequests.add(createStudentRequest);
        }
        return twentyCreateStudentRequests;
    }
}
