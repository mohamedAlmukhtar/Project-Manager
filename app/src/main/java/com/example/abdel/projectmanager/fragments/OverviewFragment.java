package com.example.abdel.projectmanager.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.abdel.projectmanager.ProjectViewModel;
import com.example.abdel.projectmanager.R;
import com.example.abdel.projectmanager.Task;
import com.example.abdel.projectmanager.TaskViewModel;
import com.example.abdel.projectmanager.adapters.OverViewAdapter;

import java.util.Date;
import java.util.List;

public class OverviewFragment extends Fragment {

    private RecyclerView upcomingRecyclerView;
    private OverViewAdapter upcomingAdapter;
    private LinearLayoutCompat tvEmptyUpcoming;
    private RelativeLayout tvUpcoming;
    private TaskViewModel mTaskViewModel;
    private ProjectViewModel mProjectViewModel;
    private TextView upcomingCount;

    private RecyclerView overdueRecyclerView;
    private OverViewAdapter overdueAdapter;
    private LinearLayoutCompat tvEmptyOverdue;
    private RelativeLayout tvOverdue;
    private TextView overdueCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);

        //upcoming
        tvEmptyUpcoming = (LinearLayoutCompat) view.findViewById(R.id.empty_upcoming_parent);
        tvUpcoming = (RelativeLayout) view.findViewById(R.id.upcoming_parent);
        upcomingCount = (TextView) view.findViewById(R.id.upcoming);

        upcomingRecyclerView = view.findViewById(R.id.upcoming_recycler_view);


        //overdue
        tvEmptyOverdue = (LinearLayoutCompat) view.findViewById(R.id.empty_overdue_parent);
        tvOverdue = (RelativeLayout) view.findViewById(R.id.overdue_parent);
        overdueCount = (TextView) view.findViewById(R.id.overdue);

        overdueRecyclerView = view.findViewById(R.id.overdue_recycler_view);


        mTaskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);

        // Create an adapter and supply the data to be displayed.
        upcomingAdapter = new OverViewAdapter(getContext(), true);

        // Connect the adapter with the recycler view.
        upcomingRecyclerView.setAdapter(upcomingAdapter);

        // Give the recycler view a default layout manager.
        upcomingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        // Create an adapter and supply the data to be displayed.
        overdueAdapter = new OverViewAdapter(getContext(), false);

        // Connect the adapter with the recycler view.
        overdueRecyclerView.setAdapter(overdueAdapter);

        // Give the recycler view a default layout manager.
        overdueRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));


        //-------------------------------------LIVE DATA OF UPCOMING TASKS---------------------------------//
        mTaskViewModel.getUpcoming(new Date().getTime(),new Date().getTime() + 1000 * 60 * 60 * 24 * 15).observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable final List<Task> tasks) {
                // Update the cached copy of the words in the adapter.
                if(tasks.isEmpty()){
                    tvUpcoming.setVisibility(View.GONE);
                    tvEmptyUpcoming.setVisibility(View.VISIBLE);
                }else{
                    upcomingCount.setText(tasks.size() + "");
                    tvUpcoming.setVisibility(View.VISIBLE);
                    tvEmptyUpcoming.setVisibility(View.GONE);

                    upcomingAdapter.setData(tasks);
                }
            }
        });


        //-------------------------------------LIVE DATA OF OVERDUE TASKS---------------------------------//
        mTaskViewModel.getOverdue(new Date().getTime()).observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable final List<Task> tasks) {
                // Update the cached copy of the words in the adapter.
                if(tasks.isEmpty()){
                    tvOverdue.setVisibility(View.GONE);
                    tvEmptyOverdue.setVisibility(View.VISIBLE);
                }else{
                    overdueCount.setText(tasks.size() + "");
                    tvOverdue.setVisibility(View.VISIBLE);
                    tvEmptyOverdue.setVisibility(View.GONE);

                    overdueAdapter.setData(tasks);
                }
            }
        });

        return view;
    }
}
