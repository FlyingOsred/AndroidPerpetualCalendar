package com.flyingosred.app.android.perpetualcalendar.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.flyingosred.app.android.perpetualcalendar.R;
import com.flyingosred.app.android.perpetualcalendar.adapter.MonthPagerAdapter;
import com.flyingosred.app.android.perpetualcalendar.util.Utils;

import java.util.Calendar;

/**
 * Copyright (C) 2016 Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */
public class MonthPagerView extends ViewPager {

    private static final int OFF_SCREEN_LIMIT = 3;

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
        initView(context);
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


    private void initView(Context context) {
        setOffscreenPageLimit(OFF_SCREEN_LIMIT);
        mAdapter = new MonthPagerAdapter(context, R.layout.recycler_view_month, R.id.recycler_view_month);
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


    private int getDiffMonths(Calendar start, Calendar end) {
        final int diffYears = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);
        return end.get(Calendar.MONTH) - start.get(Calendar.MONTH) + 12 * diffYears;
    }

    private Calendar getTempCalendarForTime(long timeInMillis) {
        if (mTempCalendar == null) {
            mTempCalendar = Calendar.getInstance();
        }
        mTempCalendar.setTimeInMillis(timeInMillis);
        return mTempCalendar;
    }

    private int getPositionFromDay(long timeInMillis) {
        final int diffMonthMax = getDiffMonths(mMinDate, mMaxDate);
        final int diffMonth = getDiffMonths(mMinDate, getTempCalendarForTime(timeInMillis));
        return Utils.constrain(diffMonth, 0, diffMonthMax);
    }

    public long getDate() {
        return mSelectedDay.getTimeInMillis();
    }


}
