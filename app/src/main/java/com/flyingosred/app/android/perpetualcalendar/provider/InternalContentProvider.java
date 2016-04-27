package com.flyingosred.app.android.perpetualcalendar.provider;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.util.Log;

import java.util.Calendar;

/**
 * Copyright (C) 2016 Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */
public class InternalContentProvider {

    private static final String LOG_TAG = InternalContentProvider.class.getSimpleName();

    private static InternalContentProvider mInstance = new InternalContentProvider();

    private final SolarTermProvider mSolarTermProvider = new SolarTermProvider();

    public static InternalContentProvider getInstance() {
        return mInstance;
    }

    private InternalContentProvider() {
    }

    public Cursor query(String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Calendar date = null;
        int count = 0;
        String[] selectionArray = selection.split(" AND ");
        for (int i = 0; i < selectionArray.length; i++) {
            String item = selectionArray[i].trim();
            if (item.contains("=?")) {
                if (item.startsWith(PerpetualCalendarContract.Date.START_DATE)) {
                    date = Calendar.getInstance();
                    date.setTimeInMillis(Long.parseLong(selectionArgs[i]));
                } else if (item.startsWith(PerpetualCalendarContract.Date.COUNT)) {
                    count = Integer.parseInt(selectionArgs[i]);
                }
            }
        }
        if (date == null || count <= 0) {
            Log.e(LOG_TAG, "Wrong selection and selectionArgs");
            return null;
        }

        Log.d(LOG_TAG, "Query for " + date.getTime() + " with count " + count);
        MatrixCursor cursor = new MatrixCursor(PerpetualCalendarContract.Date.PROJECTION_ALL, count);
        for (int i = 0; i < count; i++) {
            Object[] columns = new Object[projection.length];
            for (int j = 0; j < projection.length; j++) {
                if (projection[j].equalsIgnoreCase(PerpetualCalendarContract.Date._ID)) {
                    columns[j] = j + 1;
                } else if (projection[j].equalsIgnoreCase(PerpetualCalendarContract.Date.YEAR)) {
                    columns[j] = date.get(Calendar.YEAR);
                } else if (projection[j].equalsIgnoreCase(PerpetualCalendarContract.Date.MONTH)) {
                    columns[j] = date.get(Calendar.MONTH);
                } else if (projection[j].equalsIgnoreCase(PerpetualCalendarContract.Date.DAY)) {
                    columns[j] = date.get(Calendar.DATE);
                }
            }
            cursor.addRow(columns);
            date.add(Calendar.DATE, 1);
        }

        return cursor;
    }
}
