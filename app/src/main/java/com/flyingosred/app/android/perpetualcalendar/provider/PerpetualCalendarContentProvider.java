package com.flyingosred.app.android.perpetualcalendar.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import java.util.Calendar;

import static com.flyingosred.app.android.perpetualcalendar.provider.PerpetualCalendarContract.AUTHORITY;

public class PerpetualCalendarContentProvider extends ContentProvider {

    private static final String LOG_TAG = PerpetualCalendarContentProvider.class.getSimpleName();

    private static final String WINDOW_NAME = PerpetualCalendarContentProvider.class.getSimpleName();

    private static final int DATE_ITEM = 1;

    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, "date", DATE_ITEM);
    }

    public PerpetualCalendarContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType called uri is " + uri.toString());
        switch (URI_MATCHER.match(uri)) {
            case DATE_ITEM:
                return PerpetualCalendarContract.Date.CONTENT_TYPE;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (URI_MATCHER.match(uri)) {
            case DATE_ITEM:
                return queryDate(projection, selection, selectionArgs, sortOrder);
        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private Cursor queryDate(String[] projection, String selection,
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
