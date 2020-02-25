package com.example.abdel.projectmanager.activities;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.example.abdel.projectmanager.Project;
import com.example.abdel.projectmanager.ProjectAndAllTasks;
import com.example.abdel.projectmanager.ProjectViewModel;
import com.example.abdel.projectmanager.R;
import com.example.abdel.projectmanager.adapters.SwipeRecyclerViewAdapter;
import com.example.abdel.projectmanager.Task;
import com.example.abdel.projectmanager.TaskViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.support.v7.widget.RecyclerView.VERTICAL;
import static com.example.abdel.projectmanager.Task.Priority.LOW;
import static com.example.abdel.projectmanager.Task.Status.COMPLETED;

public class ProjectActivity extends AppCompatActivity implements SwipeRecyclerViewAdapter.CallBackInterface {

    public static final int EDIT_TASK_ACTIVITY_REQUEST_CODE = 1;
    public static final int UPDATE_TASK_ACTIVITY_REQUEST_CODE = 2;
    public static final int UPDATE_PROJECT_ACTIVITY_REQUEST_CODE = 3;

    public static final String EXTRA_DATA_UPDATE_TITLE = "extra_title_to_be_updated";
    public static final String EXTRA_DATA_UPDATE_DESC = "extra_desc_to_be_updated";
    public static final String EXTRA_DATA_UPDATE_PRIORITY = "extra_priority_to_be_updated";
    public static final String EXTRA_DATA_UPDATE_STATUS = "extra_status_to_be_updated";
    public static final String EXTRA_DATA_UPDATE_START = "extra_start_to_be_updated";
    public static final String EXTRA_DATA_UPDATE_END = "extra_end_to_be_updated";
    public static final String EXTRA_DATA_ID = "extra_data_id";
    private static final String TAG = "new task";

    private int projectID;
    private final ArrayList<Task> mDataSet = new ArrayList<>();

    SwipeRecyclerViewAdapter mAdapter = null;

    private RelativeLayout tvEmptyTextView;
    private RecyclerView mRecyclerView;
    private ProjectViewModel mProjectViewModel;
    private TaskViewModel mTaskViewModel;
    private String projectName;
    private Project mProject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        projectID = getIntent().getIntExtra("EXTRA_ID",-1);
        projectName = getIntent().getStringExtra("EXTRA_NAME");

        setTitle(projectName);

        tvEmptyTextView = (RelativeLayout) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, VERTICAL);
        mRecyclerView.addItemDecoration(itemDecor);

        mTaskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);

        mProjectViewModel = ViewModelProviders.of(this).get(ProjectViewModel.class);


        mAdapter = new SwipeRecyclerViewAdapter(this, mProjectViewModel, mTaskViewModel, projectID);

        ((SwipeRecyclerViewAdapter) mAdapter).setMode(Attributes.Mode.Single);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e("RecyclerView", "onScrollStateChanged");
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });



        mProjectViewModel.getAllTasks(projectID).observe(this, new Observer<ProjectAndAllTasks>() {
            @Override
            public void onChanged(@Nullable final ProjectAndAllTasks tasks) {
                if(tasks != null)
                    mProject = tasks.project;
                // Update the cached copy of the words in the adapter.
                if(tasks != null && tasks.getTasks().isEmpty()){
                    mRecyclerView.setVisibility(View.GONE);
                    tvEmptyTextView.setVisibility(View.VISIBLE);
                }else{
                    mRecyclerView.setVisibility(View.VISIBLE);
                    tvEmptyTextView.setVisibility(View.GONE);
                    mAdapter.setData(tasks);
                }

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProjectActivity.this, EditTaskActivity.class);
                startActivityForResult(intent, EDIT_TASK_ACTIVITY_REQUEST_CODE);

                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
    }



    //----------------------------------OPTIONS MENU-----------------------------------//

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_project, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle options menu button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete_project) {

            //DialogFragment newFragment = new alertDialogFragment();
            //newFragment.show(getSupportFragmentManager(), "delete alert");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.deleteTitle)
                    .setMessage(R.string.alert_message)
                    .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mProjectViewModel.deleteProject(projectID);
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    }).show();

        }
        else if(id == R.id.action_edit_project){
            Intent intent = new Intent(this, EditProjectActivity.class);
            intent.putExtra("EXTRA_NAME", mProject.getName());
            intent.putExtra("EXTRA_DESC", mProject.getDescription());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date start = mProject.getStartDate();
            if(start != null){
                intent.putExtra("EXTRA_START", sdf.format(start));
            }
            Date end = mProject.getDueDate();
            if(end != null){
                intent.putExtra("EXTRA_END", sdf.format(end));
            }

            intent.putExtra("EXTRA_ID", mProject.getID());
            startActivityForResult(intent, UPDATE_PROJECT_ACTIVITY_REQUEST_CODE);
        }
        else if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    //-----------------------------------BACK FROM EDIT TASK ACTIVITY-----------------------------//
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_TASK_ACTIVITY_REQUEST_CODE || requestCode == UPDATE_TASK_ACTIVITY_REQUEST_CODE){

            if(resultCode == Activity.RESULT_OK) {

                Task.Priority priority = Task.Priority.valueOf(data.getStringExtra(EditTaskActivity.EXTRA_PRIORITY));
                Task.Status status = Task.Status.valueOf(data.getStringExtra(EditTaskActivity.EXTRA_STATUS));
                String taskTitle = data.getStringExtra(EditTaskActivity.EXTRA_TITLE);
                String taskDesc = data.getStringExtra(EditTaskActivity.EXTRA_DESC);
                int statusCode = data.getIntExtra("STATUS_CHANGE", -1);

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

                if (requestCode == EDIT_TASK_ACTIVITY_REQUEST_CODE) {
                    Task task = new Task(taskTitle, startDate, endDate, priority, status, taskDesc);
                    Log.d(TAG, "onActivityResult: new task");
                    task.setProjectID(projectID);
                    mTaskViewModel.insert(task);
                    mProjectViewModel.setTotalTasks(1, projectID);
                    if (task.getStatus() == Task.Status.COMPLETED) {
                        mProjectViewModel.setCompletedTasks(1, projectID);
                    }
                    Log.d(TAG, "onActivityResult: insert task");
                    Toast.makeText(this, "Added Task : " + taskTitle, Toast.LENGTH_LONG).show();

                } else if (requestCode == UPDATE_TASK_ACTIVITY_REQUEST_CODE) {
                    int id = data.getIntExtra(EditTaskActivity.EXTRA_REPLY_ID, -1);
                    Task task = new Task(id, taskTitle, startDate, endDate, priority, status, taskDesc);
                    Log.d(TAG, "onActivityResult: new task");
                    task.setProjectID(projectID);
                    if(statusCode == 2 && status.getCode() != 2){
                        mProjectViewModel.setCompletedTasks(-1, projectID);
                    }else if(statusCode != 2 && status.getCode() == 2){
                        mProjectViewModel.setCompletedTasks(1, projectID);
                    }
                    if (id != -1) {
                        mTaskViewModel.update(task);
                        Log.d(TAG, "onActivityResult: update task");
                        Toast.makeText(this, "Update Task : " + taskTitle, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, R.string.unable_to_update,
                                Toast.LENGTH_LONG).show();
                    }
                }
            }else {
            Toast.makeText(
                    this,
                    R.string.task_not_saved,
                    Toast.LENGTH_LONG).show();
            }

        }else if(requestCode == UPDATE_PROJECT_ACTIVITY_REQUEST_CODE){

            if(resultCode == Activity.RESULT_OK) {

                String name = data.getStringExtra(EditProjectActivity.EXTRA_NAME);
                setTitle(name);
                String desc = data.getStringExtra(EditProjectActivity.EXTRA_DESC);
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
                Project project = new Project(projectID, name, desc, startDate, endDate, mProject.getTotalTasks(), mProject.getCompletedTasks());
                mProjectViewModel.update(project);

            }else {
                Toast.makeText(
                        this,
                        R.string.project_not_saved,
                        Toast.LENGTH_LONG).show();
            }
        }

    }


    //-----------------------------------HANDLE CLICKING EDIT TASK BUTTON IN SWIPE ADAPTER--------------------------//
    @Override
    public void onHandleSelection(Task task) {
        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra(EXTRA_DATA_UPDATE_TITLE, task.getTitle());
        intent.putExtra(EXTRA_DATA_UPDATE_DESC, task.getDescription());

        intent.putExtra(EXTRA_DATA_UPDATE_PRIORITY, task.getPriority().getCode());
        intent.putExtra(EXTRA_DATA_UPDATE_STATUS, task.getStatus().getCode());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        if(task.getStartDate() != null){
            String start = sdf.format(task.getStartDate());
            intent.putExtra(EXTRA_DATA_UPDATE_START, start);
        }
        if(task.getDueDate() != null){
            String end = sdf.format(task.getDueDate());
            intent.putExtra(EXTRA_DATA_UPDATE_END, end);
        }

        intent.putExtra(EXTRA_DATA_ID, task.getID());
        startActivityForResult(intent, UPDATE_TASK_ACTIVITY_REQUEST_CODE);
    }
}
