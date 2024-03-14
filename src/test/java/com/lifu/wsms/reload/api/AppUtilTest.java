package com.lifu.wsms.reload.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppUtilTest {

    @Test
    void convertLocalDateToLong() {
        LocalDate localDate = LocalDate.of(2024, 1, 1);
        long longDate = 1704067200000L;
        assertEquals(longDate, AppUtil.convertLocalDateToLong(localDate));
        assertEquals(localDate.toString(), AppUtil.convertLongToLocalDate(longDate).toString());

    }

    @Test
    void isValidStudentId() {
        String validStudentId = AppUtil.generateStudentId();
        String inValidStudentId = "KSK/2024/20002020";
        assertTrue(AppUtil.isValidStudentId(validStudentId));
        assertFalse(AppUtil.isValidStudentId(inValidStudentId));
    }
}