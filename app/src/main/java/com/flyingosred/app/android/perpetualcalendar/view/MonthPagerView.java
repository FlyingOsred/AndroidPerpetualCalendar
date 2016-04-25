package com.flyingosred.app.android.perpetualcalendar.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.flyingosred.app.android.perpetualcalendar.adapter.MonthPagerAdapter;
import com.flyingosred.app.android.perpetualcalendar.util.Utils;

import java.util.Calendar;

/**
 * Copyright (C) 2016 Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */
public class MonthPagerView extends ViewPager {

    MonthPagerAdapter mAdapter = null;

    private final Calendar mMinDate = Calendar.getInstance();

    private final Calendar mMaxDate = Calendar.getInstance();

    private final Calendar mSelectedDay = Calendar.getInstance();

    private Calendar mTempCalendar;

    public MonthPagerView(Context context) {
        this(context, null);
    }

    public MonthPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void setMinDate(long timeInMillis) {
        mMinDate.setTimeInMillis(timeInMillis);
        onRangeChanged();
    }

    public long getMinDate() {
        return mMinDate.getTimeInMillis();
    }

    public void setMaxDate(long timeInMillis) {
        mMaxDate.setTimeInMillis(timeInMillis);
        onRangeChanged();
    }

    public long getMaxDate() {
        return mMaxDate.getTimeInMillis();
    }

    public void onRangeChanged() {
        mAdapter.setRange(mMinDate, mMaxDate);

        setDate(mSelectedDay.getTimeInMillis(), false, false);
    }


    private void initView() {
        mAdapter = new MonthPagerAdapter();
        setAdapter(mAdapter);

        final Calendar tempDate = Calendar.getInstance();
        tempDate.set(Utils.DEFAULT_START_YEAR, Calendar.JANUARY, 1);
        final long minDateMillis = tempDate.getTimeInMillis();
        tempDate.set(Utils.DEFAULT_END_YEAR, Calendar.DECEMBER, 31);
        final long maxDateMillis = tempDate.getTimeInMillis();

        if (maxDateMillis < minDateMillis) {
            throw new IllegalArgumentException("maxDate must be >= minDate");
        }

        setMinDate(minDateMillis);
        setMaxDate(maxDateMillis);
    }

    private void setDate(long timeInMillis, boolean animate, boolean setSelected) {
        if (setSelected) {
            mSelectedDay.setTimeInMillis(timeInMillis);
        }

        final int position = getPositionFromDay(timeInMillis);
        if (position != getCurrentItem()) {
            setCurrentItem(position, animate);
        }

        mTempCalendar.setTimeInMillis(timeInMillis);
        mAdapter.setSelectedDay(mTempCalendar);
    }

    private int getPositionFromDay(long timeInMillis) {
        final int diffMonthMax = getDiffMonths(mMinDate, mMaxDate);
        final int diffMonth = getDiffMonths(mMinDate, getTempCalendarForTime(timeInMillis));
        return MathUtils.constrain(diffMonth, 0, diffMonthMax);
    }

    public long getDate() {
        return mSelectedDay.getTimeInMillis();
    }
}
