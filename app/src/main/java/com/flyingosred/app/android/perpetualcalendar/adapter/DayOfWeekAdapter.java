package com.flyingosred.app.android.perpetualcalendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyingosred.app.android.perpetualcalendar.R;
import com.flyingosred.app.android.perpetualcalendar.util.Utils;

import java.util.Calendar;
import java.util.Locale;

/**
 * Copyright (C) 2016 Osred Brockhoist <osred.brockhoist@hotmail.com>. All Rights Reserved.
 */
public class DayOfWeekAdapter extends RecyclerView.Adapter {

    private int mFirstDayOfWeek = Calendar.SUNDAY;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView view = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_day_of_week, parent, false);
        DayOfWeekViewHolder viewHolder = new DayOfWeekViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DayOfWeekViewHolder viewHolder = (DayOfWeekViewHolder) holder;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, mFirstDayOfWeek + position);
        String dayOfWeekString = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT,
                Locale.getDefault()).toUpperCase(Locale.getDefault());
        viewHolder.mTextView.setText(dayOfWeekString);
    }

    @Override
    public int getItemCount() {
        return Utils.DAYS_PER_WEEK;
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        if (mFirstDayOfWeek == firstDayOfWeek) {
            return;
        }
        mFirstDayOfWeek = firstDayOfWeek;
        notifyDataSetChanged();
    }

    private class DayOfWeekViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;

        public DayOfWeekViewHolder(TextView itemView) {
            super(itemView);
            mTextView = itemView;
        }
    }
}
