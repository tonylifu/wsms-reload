package com.lifu.wsms.reload.util;

import com.lifu.wsms.reload.dto.Address;
import com.lifu.wsms.reload.dto.Contact;
import com.lifu.wsms.reload.dto.LegalGuardian;
import com.lifu.wsms.reload.dto.request.student.CreateStudentRequest;
import com.lifu.wsms.reload.dto.request.student.UpdateStudentRequest;
import com.lifu.wsms.reload.dto.request.user.CreateUserRequest;
import com.lifu.wsms.reload.dto.request.user.UpdateUserRequest;
import com.lifu.wsms.reload.entity.student.Student;
import com.lifu.wsms.reload.enums.Gender;
import com.lifu.wsms.reload.enums.UserRole;
import com.lifu.wsms.reload.enums.UserStatus;
import com.lifu.wsms.reload.mapper.student.CreateStudentRequestToStudentMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SudentTestUtil {
    private SudentTestUtil() {}

    /**
     * Generates a {@link CreateStudentRequest} object with predefined values.
     *
     * @return A {@link CreateStudentRequest} object populated with predefined values.
     */
    public static CreateStudentRequest getCreateStudentRequest() {
        return CreateStudentRequest.builder()
                .firstName("David")
                .middleName("Owogoga")
                .lastName("Lifu")
                .dob("2010-01-01")
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
     * Generates a {@link Student} object by mapping the values from a {@link CreateStudentRequest} object using the {@link CreateStudentRequestToStudentMapper}.
     *
     * @return A {@link Student} object populated with values from a predefined {@link CreateStudentRequest} object.
     */
    public static Student getStudent() {
        return CreateStudentRequestToStudentMapper.INSTANCE.toStudent(getCreateStudentRequest());
    }

    /**
     * Generates an {@link UpdateStudentRequest} object with predefined values for testing purposes.
     *
     * @return An {@link UpdateStudentRequest} object with predefined values.
     */
    public static UpdateStudentRequest getUpdateStudentRequest() {
        return UpdateStudentRequest.builder()
                .studentId("KSK-2024-1234")
                .firstName("Joan")
                .middleName("Owogbuo")
                .lastName("Lifu")
                .dob("2010-01-01")
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
     * Generates a list of twenty {@link CreateStudentRequest} objects with student IDs ranging from 'KSK-2024-0001' to 'KSK-2024-0020'.
     *
     * @return A list of {@link CreateStudentRequest} objects containing twenty student requests.
     */
    public static List<CreateStudentRequest> getTwentyCreateStudentsRequests() {
        List<CreateStudentRequest> twentyCreateStudentRequests = new ArrayList<>();
        //students KSK/2024/0001 - KSK/2024/0020
        //var prefix = "KSK-2024-";
        for (int i = 1; i <= 20; i++) {
            //String studentId = prefix + String.format("%04d", i);
            var createStudentRequest = getCreateStudentRequest();
            //createStudentRequest.setStudentId(studentId);
            twentyCreateStudentRequests.add(createStudentRequest);
        }
        return twentyCreateStudentRequests;
    }

    /**
     * Generates a JSON string representing a sample request for creating a student.
     * The generated JSON string contains dummy data for demonstration purposes.
     *
     * @return a JSON string representing a sample request for creating a student.
     */
    public static String getCreateStudentRequestJsonString() {
        var request = "{\n" +
                "    \"studentId\": \"KSK-2024-1234\",\n" +
                "    \"firstName\": \"David\",\n" +
                "    \"middleName\": \"Owogoga\",\n" +
                "    \"lastName\": \"Lifu\",\n" +
                "    \"dob\": \"2010-01-01\",\n" +
                "    \"gender\": \"male\",\n" +
                "    \"address\": {\n" +
                "        \"houseNumber\": \"21\",\n" +
                "        \"streetName\": \"Lyon Crescent\",\n" +
                "        \"area\": \"Stirling\",\n" +
                "        \"localGovtArea\": \"Stirling\",\n" +
                "        \"state\": \"Scotland\",\n" +
                "        \"country\": \"United Kingdom\"\n" +
                "    },\n" +
                "    \"contact\": {\n" +
                "        \"email\": \"davidlifu@gmail.com\",\n" +
                "        \"mobilePhone\": \"07766433489\",\n" +
                "        \"telephone\": \"\"\n" +
                "    },\n" +
                "    \"legalGuardian\": {\n" +
                "        \"biologicalParentListed\": true,\n" +
                "        \"father\": \"Anthony Lifu\",\n" +
                "        \"fatherContactInformation\": {\n" +
                "            \"email\": \"tlifu75@gmail.com\",\n" +
                "            \"mobilePhone\": \"07766433489\",\n" +
                "            \"telephone\": \"\"\n" +
                "        },\n" +
                "        \"mother\": \"\",\n" +
                "        \"motherContactInformation\": {\n" +
                "            \"email\": \"ladilifu@gmail.com\",\n" +
                "            \"mobilePhone\": \"07766433489\",\n" +
                "            \"telephone\": \"\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"currentGrade\": \"SSS120241\",\n" +
                "    \"isDisabled\": false,\n" +
                "    \"disabilityDetails\": \"NA\"\n" +
                "}";
        return request;
    }

    public static CreateUserRequest getCreateUserDTO() {
        return CreateUserRequest.builder()
                .username("user1")
                .mobile("077664333489")
                .email("test1@test.com")
                .designation("Administrator")
                .status(UserStatus.CREATED)
                .roles(Set.of(UserRole.ADMIN, UserRole.BURSAR))
                .build();
    }

    public static UpdateUserRequest getUpdateUserDTO() {
        return UpdateUserRequest.builder()
                .username("user2")
                .mobile("01234456677")
                .email("test2@test.com")
                .designation("Bursar")
                .roles(Set.of(UserRole.BURSAR))
                .build();
    }
}
