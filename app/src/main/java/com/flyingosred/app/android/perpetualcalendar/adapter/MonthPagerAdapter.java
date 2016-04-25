package com.flyingosred.app.android.perpetualcalendar.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.flyingosred.app.android.perpetualcalendar.util.Utils;

import java.util.Calendar;

/**
 * Copyright (C) 2016 Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */
public class MonthPagerAdapter extends PagerAdapter {

    private int mCount = 0;

    private final Calendar mMinDate = Calendar.getInstance();

    private final Calendar mMaxDate = Calendar.getInstance();

    private Calendar mSelectedDay = null;

    public MonthPagerAdapter() {
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }

    public void setRange(@NonNull Calendar min, @NonNull Calendar max) {
        mMinDate.setTimeInMillis(min.getTimeInMillis());
        mMaxDate.setTimeInMillis(max.getTimeInMillis());

        final int diffYear = mMaxDate.get(Calendar.YEAR) - mMinDate.get(Calendar.YEAR);
        final int diffMonth = mMaxDate.get(Calendar.MONTH) - mMinDate.get(Calendar.MONTH);
        mCount = diffMonth + Utils.MONTHS_IN_YEAR * diffYear + 1;

        notifyDataSetChanged();
    }

    public void setSelectedDay(@Nullable Calendar day) {
        final int oldPosition = getPositionForDay(mSelectedDay);
        final int newPosition = getPositionForDay(day);

        if (oldPosition != newPosition && oldPosition >= 0) {
            final ViewHolder oldMonthView = mItems.get(oldPosition, null);
            if (oldMonthView != null) {
                oldMonthView.calendar.setSelectedDay(-1);
            }
        }

        if (newPosition >= 0) {
            final ViewHolder newMonthView = mItems.get(newPosition, null);
            if (newMonthView != null) {
                final int dayOfMonth = day.get(Calendar.DAY_OF_MONTH);
                newMonthView.calendar.setSelectedDay(dayOfMonth);
            }
        }

        mSelectedDay = day;
    }

    private int getPositionForDay(@Nullable Calendar day) {
        if (day == null) {
            return -1;
        }

        final int yearOffset = day.get(Calendar.YEAR) - mMinDate.get(Calendar.YEAR);
        final int monthOffset = day.get(Calendar.MONTH) - mMinDate.get(Calendar.MONTH);
        final int position = yearOffset * Utils.MONTHS_IN_YEAR + monthOffset;
        return position;
    }

    private class ViewHolder {
        public final int position;
        public final View container;
        public final SimpleMonthView calendar;

        public ViewHolder(int position, View container, SimpleMonthView calendar) {
            this.position = position;
            this.container = container;
            this.calendar = calendar;
        }
    }

}
