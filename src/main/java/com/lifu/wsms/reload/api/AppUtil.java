package com.lifu.wsms.reload.api;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

public class AppUtil {
    private AppUtil(){}

    /**
     * This method concerts local date to long
     * @param localDate
     * @return long
     */
    public static long convertLocalDateToLong(LocalDate localDate) {
        return localDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    /**
     * This method converts long date to local date
     * @param date
     * @return LocalDate
     */
    public static LocalDate convertLongToLocalDate(long date) {
        return Instant.ofEpochMilli(date).atZone(ZoneOffset.UTC).toLocalDate();
    }
}
