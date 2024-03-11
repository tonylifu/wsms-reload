package com.lifu.wsms.reload.api;

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
}
