package com.example.abdel.projectmanager.fragments;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.abdel.projectmanager.Project;
import com.example.abdel.projectmanager.ProjectViewModel;
import com.example.abdel.projectmanager.R;
import com.example.abdel.projectmanager.activities.EditProjectActivity;
import com.example.abdel.projectmanager.adapters.ProjectsRecyclerAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ProjectFragment extends Fragment {

    public static final int EDIT_PROJECT_ACTIVITY_REQUEST_CODE = 1;

    //Fields
    private RecyclerView mRecyclerView;
    private ProjectsRecyclerAdapter mAdapter;
    public List<Project> mProjectList;
    private ProjectViewModel mProjectViewModel;
    boolean isDone = false;
    private RelativeLayout tvEmptyTextView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project,container,false);

        tvEmptyTextView = (RelativeLayout) view.findViewById(R.id.empty_view);

        // Create recycler view.
        mRecyclerView = view.findViewById(R.id.rec);

        mProjectViewModel = ViewModelProviders.of(this).get(ProjectViewModel.class);

        // Create an adapter and supply the data to be displayed.
        mAdapter = new ProjectsRecyclerAdapter(getContext());
        // Connect the adapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        //-----------------------------------LIVE DATA OF PROJECT LIST--------------------------------//
        mProjectViewModel.getAllProjects().observe(this, new Observer<List<Project>>() {
            @Override
            public void onChanged(@Nullable final List<Project> projects) {
                // Update the cached copy of the Projects in the adapter.
                if(projects.isEmpty()){
                    mRecyclerView.setVisibility(View.GONE);
                    tvEmptyTextView.setVisibility(View.VISIBLE);
                }else{
                    mRecyclerView.setVisibility(View.VISIBLE);
                    tvEmptyTextView.setVisibility(View.GONE);
                    mAdapter.setData(projects);
                }
            }
        });



        //----------------------------------ADD NEW PROJECT BUTTON-------------------------------------//
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), EditProjectActivity.class);
                startActivityForResult(intent, EDIT_PROJECT_ACTIVITY_REQUEST_CODE);


            }
        });

        return view;
    }


    //------------------------------------Back from Edit Project Activity----------------------------//

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_PROJECT_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String start = data.getStringExtra(EditProjectActivity.EXTRA_START);
            String end = data.getStringExtra(EditProjectActivity.EXTRA_END);
            Date startDate = null, endDate = null;

            try {
                startDate = !start.isEmpty() ? sdf.parse(start) : null;
                endDate = !end.isEmpty() ? sdf.parse(end) : null;

            } catch (ParseException e) {
                e.printStackTrace();
            }

            //Inserting new project to database
            Project project = new Project(data.getStringExtra(EditProjectActivity.EXTRA_NAME), data.getStringExtra(EditProjectActivity.EXTRA_DESC), startDate, endDate, 0, 0);
            mProjectViewModel.insert(project);

        } else {
            Toast.makeText(
                    getContext(),
                    R.string.project_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}
