package com.lifu.wsms.reload.service.student;

import com.lifu.wsms.reload.api.AppUtil;
import com.lifu.wsms.reload.api.contract.student.StudentNumberService;
import com.lifu.wsms.reload.repository.StudentNumberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class StudentNumberGeneratorTest {
    @Autowired
    private StudentNumberService studentNumberService;
    @Autowired
    private StudentNumberRepository studentNumberRepository;

    @Test
    @Transactional
    void shouldReturnIncrementalAutoGeneratedStudentIdBaseOnCurrentYear() throws InterruptedException {
        //Given
        int currentYear1 = 2020;
        int currentYear2 = 2021;
        int currentYear3 = 2022;

        //When -> when studentId is generated given the current year
        String studentIdOne1 = AppUtil.generateStudentId(currentYear1,
                studentNumberService.generateNextStudentId(currentYear1));
        String studentIdOne2 = AppUtil.generateStudentId(currentYear1,
                studentNumberService.generateNextStudentId(currentYear1));
        String studentIdOne3 = AppUtil.generateStudentId(currentYear1,
                studentNumberService.generateNextStudentId(currentYear1));

        String studentIdTwo1 = AppUtil.generateStudentId(currentYear2,
                studentNumberService.generateNextStudentId(currentYear2));
        String studentIdTwo2 = AppUtil.generateStudentId(currentYear2,
                studentNumberService.generateNextStudentId(currentYear2));
        String studentIdTwo3 = AppUtil.generateStudentId(currentYear2,
                studentNumberService.generateNextStudentId(currentYear2));

        String studentIdThree1 = AppUtil.generateStudentId(currentYear3,
                studentNumberService.generateNextStudentId(currentYear3));
        String studentIdThree2 = AppUtil.generateStudentId(currentYear3,
                studentNumberService.generateNextStudentId(currentYear3));
        String studentIdThree3 = AppUtil.generateStudentId(currentYear3,
                studentNumberService.generateNextStudentId(currentYear3));

        //Then -> generated studentId should be sequential based on the year
        assertThat(studentIdOne1).isEqualTo("KSK-2020-0001");
        assertThat(studentIdOne2).isEqualTo("KSK-2020-0002");
        assertThat(studentIdOne3).isEqualTo("KSK-2020-0003");

        assertThat(studentIdTwo1).isEqualTo("KSK-2021-0001");
        assertThat(studentIdTwo2).isEqualTo("KSK-2021-0002");
        assertThat(studentIdTwo3).isEqualTo("KSK-2021-0003");

        assertThat(studentIdThree1).isEqualTo("KSK-2022-0001");
        assertThat(studentIdThree2).isEqualTo("KSK-2022-0002");
        assertThat(studentIdThree3).isEqualTo("KSK-2022-0003");

        //Then -> Clean up
        var allIds = List.of(20200001L, 20200002L, 20200003L,
                20210001L, 20210002L, 20210003L,
                20220001L, 20220002L, 20220003L);
        studentNumberRepository.deleteAllById(allIds);
    }
}