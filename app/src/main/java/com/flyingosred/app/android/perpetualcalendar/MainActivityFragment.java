package com.flyingosred.app.android.perpetualcalendar;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyingosred.app.android.perpetualcalendar.adapter.DayOfWeekAdapter;
import com.flyingosred.app.android.perpetualcalendar.util.Utils;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView dayOfWeekView = (RecyclerView) getView().findViewById(R.id.recycler_view_day_of_week);
        dayOfWeekView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), Utils.DAYS_PER_WEEK);
        dayOfWeekView.setLayoutManager(layoutManager);
        DayOfWeekAdapter adapter = new DayOfWeekAdapter();
        dayOfWeekView.setAdapter(adapter);
    }
}
