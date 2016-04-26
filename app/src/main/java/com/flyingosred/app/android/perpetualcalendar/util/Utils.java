package com.flyingosred.app.android.perpetualcalendar.util;

import java.util.Calendar;

/**
 * Copyright (C) 2016 Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */
public final class Utils {

    public static final int DAYS_PER_WEEK = 7;

    public static final int MONTHS_IN_YEAR = 12;

    public static final int DEFAULT_START_YEAR = 1901;

    public static final int DEFAULT_END_YEAR = 2099;

    public static int constrain(int amount, int low, int high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

    public static boolean isValidYear(int year) {
        return year >= DEFAULT_START_YEAR && year <= DEFAULT_END_YEAR;
    }

    private static boolean isValidMonth(int month) {
        return month >= Calendar.JANUARY && month <= Calendar.DECEMBER;
    }
}
