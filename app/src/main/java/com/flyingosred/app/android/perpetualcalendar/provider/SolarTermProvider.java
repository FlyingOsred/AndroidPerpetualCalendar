package com.flyingosred.app.android.perpetualcalendar.provider;

import com.flyingosred.app.android.perpetualcalendar.database.SolarTermDatabase;
import com.flyingosred.app.android.perpetualcalendar.database.SolarTermDatabaseItem;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Copyright (C) 2016 Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */
public class SolarTermProvider {
    private HashMap<SolarTermDatabaseItem, Integer> mSolarTermMap = new HashMap<>();
    private Locale mLocale = null;

    public SolarTermProvider() {
        init();
    }

    public void reset() {
        mSolarTermMap.clear();
        init();
    }

    public int get(Calendar calendar) {
        if (mLocale == null || !Locale.getDefault().equals(mLocale)) {
            reset();
        }
        SolarTermDatabaseItem item = new SolarTermDatabaseItem(calendar);
        if (mSolarTermMap.containsKey(item)) {
            return mSolarTermMap.get(item);
        }
        return PerpetualCalendarContract.INVALID_ID;
    }

    private void init() {
        for (int i = 0; i < SolarTermDatabase.DATABASE.length; i++) {
            int year = SolarTermDatabase.START_YEAR + i;
            int[][] yearData = SolarTermDatabase.DATABASE[i];
            for (int j = 0; j < yearData.length; j++) {
                int[] dateData = yearData[j];
                int month = dateData[0] - 1;
                int day = dateData[1];
                int hour = dateData[2];
                int minute = dateData[3];
                SolarTermDatabaseItem item = new SolarTermDatabaseItem(
                        year, month, day, hour, minute, 0,
                        TimeZone.getTimeZone(SolarTermDatabase.TIMEZONE));
                mSolarTermMap.put(item, j);
            }
        }
        mLocale = Locale.getDefault();
    }
}
