package com.flyingosred.app.android.perpetualcalendar.provider;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.Calendar;

/**
 * Copyright (C) 2016 Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */
public final class PerpetualCalendarContract {

    public static final String AUTHORITY = "com.flyingosred.app.android.perpetualcalendar.provider";

    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public static final int INVALID_ID = -1;

    public static final class Date implements BaseColumns {
        private Date() {
        }

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "date");

        public static final String CONTENT_TYPE = "com.flyingosred.app.android.perpetualcalendar.provider.date_item";

        public static final String _ID = "_id";
        public static final String YEAR = "year";
        public static final String MONTH = "month";
        public static final String DAY = "day";
        public static final String LUNAR_YEAR = "lunar_year";
        public static final String LUNAR_MONTH = "lunar_month";
        public static final String LUNAR_DAY = "lunar_day";
        public static final String SOLAR_TERM = "solar_term";
        public static final String HOLIDAY = "holiday";

        public static final String START_DATE = "start_date";
        public static final String COUNT = "count";

        public static final String[] PROJECTION_ALL = {_ID, YEAR, MONTH, DAY, LUNAR_YEAR, LUNAR_MONTH, LUNAR_DAY, SOLAR_TERM, HOLIDAY};

        public Cursor query(Context context, int year, int month, int day, int count) {
            StringBuilder selection = new StringBuilder();
            selection.append(YEAR);
            selection.append("=?");
            selection.append(" AND ");
            selection.append(MONTH);
            selection.append("=?");
            selection.append(" AND ");
            selection.append(DAY);
            selection.append("=?");
            selection.append(" AND ");
            selection.append(COUNT);
            selection.append("=?");
            String[] selectionArgs = {String.valueOf(year), String.valueOf(month), String.valueOf(day), String.valueOf(count)};
            return context.getContentResolver().query(CONTENT_URI, PROJECTION_ALL, selection.toString(), selectionArgs, null);
        }

        public static Cursor query(Context context, Calendar date, int count) {
            StringBuilder selection = new StringBuilder();
            selection.append(START_DATE);
            selection.append("=?");
            selection.append(" AND ");
            selection.append(COUNT);
            selection.append("=?");
            String[] selectionArgs = {String.valueOf(date.getTimeInMillis()), String.valueOf(count)};
            return context.getContentResolver().query(CONTENT_URI, PROJECTION_ALL, selection.toString(), selectionArgs, null);
        }
    }
}
