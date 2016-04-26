package com.flyingosred.app.android.perpetualcalendar.adapter;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyingosred.app.android.perpetualcalendar.R;
import com.flyingosred.app.android.perpetualcalendar.provider.PerpetualCalendarContract;
import com.flyingosred.app.android.perpetualcalendar.util.Utils;

import java.util.Calendar;

/**
 * Copyright (C) 2016 Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */
public class MonthAdapter extends RecyclerView.Adapter {

    private static final String LOG_TAG = MonthAdapter.class.getSimpleName();

    private static final int DEFAULT_SELECTED_DAY = -1;

    private final Calendar mCalendar = Calendar.getInstance();

    private static final int DEFAULT_WEEK_START = Calendar.SUNDAY;

    private final Context mContext;

    private int mActivatedDay = -1;

    private int mMonth;

    private int mYear;

    private int mDayOfWeekStart;

    private int mToday = DEFAULT_SELECTED_DAY;

    private int mDaysInMonth;

    private int mEnabledDayStart = 1;

    private int mEnabledDayEnd = 31;

    private int mWeekStart = DEFAULT_WEEK_START;

    private Cursor mCursor = null;

    public MonthAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_month_day, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mCursor != null && mCursor.getCount() > position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            mCursor.moveToPosition(position);
            int day = mCursor.getInt(mCursor.getColumnIndex(PerpetualCalendarContract.Date.DAY));
            viewHolder.mDateTextView.setText(String.valueOf(day));
        }
    }

    @Override
    public int getItemCount() {
        return Utils.DAYS_PER_WEEK * 6;
    }

    public void setMonthParams(int selectedDay, int month, int year, int weekStart, int enabledDayStart,
                               int enabledDayEnd) {
        Log.d(LOG_TAG, "selectedDay=" + selectedDay + ", month=" + month + ", year=" + year
                + ", weekStart=" + weekStart + ", enabledDayStart=" + enabledDayStart
                + ", enabledDayEnd=" + enabledDayEnd);
        mActivatedDay = selectedDay;

        if (isValidMonth(month)) {
            mMonth = month;
        }
        mYear = year;

        mCalendar.set(Calendar.MONTH, mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        mDayOfWeekStart = mCalendar.get(Calendar.DAY_OF_WEEK);

        if (isValidDayOfWeek(weekStart)) {
            mWeekStart = weekStart;
        } else {
            mWeekStart = mCalendar.getFirstDayOfWeek();
        }

        final Calendar today = Calendar.getInstance();
        mToday = -1;
        mDaysInMonth = getDaysInMonth(mMonth, mYear);
        for (int i = 0; i < mDaysInMonth; i++) {
            final int day = i + 1;
            if (sameDay(day, today)) {
                mToday = day;
            }
        }

        mEnabledDayStart = Utils.constrain(enabledDayStart, 1, mDaysInMonth);
        mEnabledDayEnd = Utils.constrain(enabledDayEnd, mEnabledDayStart, mDaysInMonth);

        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(mCalendar.getTimeInMillis());
        startDate.add(Calendar.DATE, 0 - findDayOffset());
        new QueryAsyncTask(mContext, startDate).execute();
    }

    private static boolean isValidDayOfWeek(int day) {
        return day >= Calendar.SUNDAY && day <= Calendar.SATURDAY;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mItemView;
        public TextView mDateTextView;
        public TextView mInfoTextView;
        public TextView mTodayTextView;
        public TextView mOffOrWorkTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mDateTextView = (TextView) itemView.findViewById(R.id.month_day_date_text_view);
            mInfoTextView = (TextView) itemView.findViewById(R.id.month_day_info_text_view);
            mTodayTextView = (TextView) itemView.findViewById(R.id.month_day_today_text_view);
            mOffOrWorkTextView = (TextView) itemView.findViewById(R.id.month_day_off_or_work_text_view);
        }
    }

    private static boolean isValidMonth(int month) {
        return month >= Calendar.JANUARY && month <= Calendar.DECEMBER;
    }

    private static int getDaysInMonth(int month, int year) {
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.MARCH:
            case Calendar.MAY:
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.OCTOBER:
            case Calendar.DECEMBER:
                return 31;
            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
            case Calendar.NOVEMBER:
                return 30;
            case Calendar.FEBRUARY:
                return (year % 4 == 0) ? 29 : 28;
            default:
                throw new IllegalArgumentException("Invalid Month");
        }
    }

    private boolean sameDay(int day, Calendar today) {
        return mYear == today.get(Calendar.YEAR) && mMonth == today.get(Calendar.MONTH)
                && day == today.get(Calendar.DAY_OF_MONTH);
    }

    private int findDayOffset() {
        final int offset = mDayOfWeekStart - mWeekStart;
        if (mDayOfWeekStart < mWeekStart) {
            return offset + Utils.DAYS_PER_WEEK;
        }
        return offset;
    }

    private class QueryAsyncTask extends AsyncTask<Void, Void, Cursor> {

        private final Context mContext;

        private final Calendar mCalendar;

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            Log.d(LOG_TAG, "onPostExecute query done");
            swapCursor(cursor, true);
        }

        public QueryAsyncTask(Context context, Calendar calendar) {
            mContext = context;
            mCalendar = calendar;
        }

        @Override
        protected Cursor doInBackground(Void... params) {
            Log.d(LOG_TAG, "doInBackground starting query");
            return PerpetualCalendarContract.Date.query(mContext, mCalendar, Utils.DAYS_PER_WEEK * 6);
        }
    }

    public void swapCursor(Cursor newCursor, boolean notify) {
        if (newCursor == mCursor) {
            return;
        }
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        mCursor = newCursor;
        if (notify) {
            notifyDataSetChanged();
        }
    }
}
