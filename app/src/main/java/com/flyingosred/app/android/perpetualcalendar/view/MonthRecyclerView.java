package com.flyingosred.app.android.perpetualcalendar.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.flyingosred.app.android.perpetualcalendar.adapter.MonthAdapter;
import com.flyingosred.app.android.perpetualcalendar.util.Utils;

import java.util.Calendar;

/**
 * Copyright (C) 2016 Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */
public class MonthRecyclerView extends RecyclerView {

    private static final String LOG_TAG = MonthRecyclerView.class.getSimpleName();

    private static final int DEFAULT_WEEK_START = Calendar.SUNDAY;

    private static final int DEFAULT_SELECTED_DAY = -1;

    private OnDayClickListener mOnDayClickListener;

    private int mWeekStart = DEFAULT_WEEK_START;

    private final Calendar mCalendar = Calendar.getInstance();

    private int mActivatedDay = -1;

    private int mMonth;

    private int mYear;

    private int mDayOfWeekStart;

    private int mToday = DEFAULT_SELECTED_DAY;

    private int mDaysInMonth;

    private int mEnabledDayStart = 1;

    private int mEnabledDayEnd = 31;

    private MonthAdapter mAdapter = null;

    public MonthRecyclerView(Context context) {
        this(context, null);
    }

    public MonthRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public void setOnDayClickListener(OnDayClickListener listener) {
        mOnDayClickListener = listener;
    }

    public interface OnDayClickListener {
        void onDayClick(MonthRecyclerView view, Calendar day);
    }

    public void setFirstDayOfWeek(int weekStart) {
        if (isValidDayOfWeek(weekStart)) {
            mWeekStart = weekStart;
        } else {
            mWeekStart = mCalendar.getFirstDayOfWeek();
        }
    }

    public void setSelectedDay(int dayOfMonth) {
        mActivatedDay = dayOfMonth;
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
        mAdapter.setMonthParams(selectedDay, month, year, weekStart, enabledDayStart, enabledDayEnd);
    }

    private static boolean isValidDayOfWeek(int day) {
        return day >= Calendar.SUNDAY && day <= Calendar.SATURDAY;
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

    private void initView(Context context) {
        setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), Utils.DAYS_PER_WEEK) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        setLayoutManager(layoutManager);
        mAdapter = new MonthAdapter(context);
        setAdapter(mAdapter);
    }
}
