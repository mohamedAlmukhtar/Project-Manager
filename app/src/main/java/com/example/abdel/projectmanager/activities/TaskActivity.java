package com.example.abdel.projectmanager.activities;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abdel.projectmanager.ProjectViewModel;
import com.example.abdel.projectmanager.R;
import com.example.abdel.projectmanager.Task;
import com.example.abdel.projectmanager.TaskViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.abdel.projectmanager.activities.ProjectActivity.EXTRA_DATA_ID;
import static com.example.abdel.projectmanager.activities.ProjectActivity.EXTRA_DATA_UPDATE_DESC;
import static com.example.abdel.projectmanager.activities.ProjectActivity.EXTRA_DATA_UPDATE_END;
import static com.example.abdel.projectmanager.activities.ProjectActivity.EXTRA_DATA_UPDATE_PRIORITY;
import static com.example.abdel.projectmanager.activities.ProjectActivity.EXTRA_DATA_UPDATE_START;
import static com.example.abdel.projectmanager.activities.ProjectActivity.EXTRA_DATA_UPDATE_STATUS;
import static com.example.abdel.projectmanager.activities.ProjectActivity.EXTRA_DATA_UPDATE_TITLE;
import static com.example.abdel.projectmanager.activities.ProjectActivity.UPDATE_TASK_ACTIVITY_REQUEST_CODE;

public class TaskActivity extends AppCompatActivity {


    //----------------------------------Fields------------------------------------//

    //Data
    private int taskID;
    private String mTitle;
    private String mPriority;
    private int mStatus;
    private String mStartDate;
    private String mEndDate;
    private String mDescription;
    private int projectID;

    //TextViews
    private TextView tvTitle;
    private TextView tvPriority;
    private TextView tvStatus;
    private TextView tvStartDate;
    private TextView tvEndDate;
    private TextView tvDescription;

    private TaskViewModel mTaskViewModel;
    private ProjectViewModel mProjectViewModel;

    private int priorityCode = 0;
    private String mActivityLabel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        Bundle extras = intent.getExtras();

        mActivityLabel = extras.getString("EXTRA_LABEL");

        setTitle(mActivityLabel);

        tvTitle = findViewById(R.id.show_title);
        tvPriority = findViewById(R.id.show_priority);
        tvStatus = findViewById(R.id.show_status);
        tvStartDate = findViewById(R.id.show_startdate);
        tvEndDate = findViewById(R.id.show_enddate);
        tvDescription = findViewById(R.id.show_description);
        mTaskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        mProjectViewModel = ViewModelProviders.of(this).get(ProjectViewModel.class);


        mTitle = extras.getString("EXTRA_NAME");


        taskID = extras.getInt("EXTRA_ID",-1);

        projectID = extras.getInt("EXTRA_PRO", -1);

        mPriority = extras.getString("EXTRA_PRIORITY");

        mStatus = extras.getInt("EXTRA_STATUS", -1);


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String intentstart = extras.getString("EXTRA_START");
        String intentend = extras.getString("EXTRA_END");

        if(intentstart != null && !intentstart.isEmpty())
            mStartDate = sdf.format(new Date(intentstart));

        if(intentend != null && !intentend.isEmpty())
            mEndDate = sdf.format(new Date(intentend));


        mDescription = extras.getString("EXTRA_DESC");

        try {
            updateData();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }//END OF ON CREATE



    //-------------------------------------OPTIONS MENU----------------------------------------//

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete_task) {

            //DialogFragment newFragment = new alertDialogFragment();
            //newFragment.show(getSupportFragmentManager(), "delete alert");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.delete_task_title)
                    .setMessage("Are you sure you want to delete this task: " + mTitle)
                    .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mTaskViewModel.deleteFromTasks(taskID);
                            mProjectViewModel.setTotalTasks(-1,projectID);
                            if (mStatus == 2){
                                mProjectViewModel.setCompletedTasks(-1,projectID);
                            }
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    }).show();

        }
        else if(id == R.id.action_edit_task){
            Intent intent = new Intent(this, EditTaskActivity.class);
            intent.putExtra(EXTRA_DATA_UPDATE_TITLE, mTitle);
            intent.putExtra(EXTRA_DATA_UPDATE_DESC, mDescription);

            intent.putExtra(EXTRA_DATA_UPDATE_PRIORITY, priorityCode);
            intent.putExtra(EXTRA_DATA_UPDATE_STATUS, mStatus);

            if(mStartDate != null && !mStartDate.isEmpty()){
                String start = mStartDate;
                intent.putExtra(EXTRA_DATA_UPDATE_START, start);
            }
            if(mEndDate != null && !mEndDate.isEmpty()){
                String end = mEndDate;
                intent.putExtra(EXTRA_DATA_UPDATE_END, end);
            }
            intent.putExtra(EXTRA_DATA_ID, taskID);
            startActivityForResult(intent, UPDATE_TASK_ACTIVITY_REQUEST_CODE);
        }
        else if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    //-----------------------------------BACK FROM EDIT TASK ACTIVITY-----------------------------//
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){

            Task.Priority priority = Task.Priority.valueOf(data.getStringExtra(EditTaskActivity.EXTRA_PRIORITY));
            mPriority = priority.toString().toLowerCase();

            Task.Status status = Task.Status.valueOf(data.getStringExtra(EditTaskActivity.EXTRA_STATUS));
            mStatus = status.getCode();

            int statusCode = data.getIntExtra("STATUS_CHANGE", -1);

            String taskTitle = data.getStringExtra(EditTaskActivity.EXTRA_TITLE);
            mTitle = taskTitle;

            String taskDesc = data.getStringExtra(EditTaskActivity.EXTRA_DESC);
            mDescription = taskDesc;

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String start = data.getStringExtra(EditTaskActivity.EXTRA_START);
            mStartDate = start;
            String end = data.getStringExtra(EditTaskActivity.EXTRA_END);
            mEndDate = end;

            Date startDate = null, endDate = null;
            try {
                startDate = !start.isEmpty() ? sdf.parse(start) : null;
                endDate = !end.isEmpty() ? sdf.parse(end) : null;

            } catch (ParseException e) {
                e.printStackTrace();
            }


            int id = data.getIntExtra(EditTaskActivity.EXTRA_REPLY_ID, -1);
            Task task = new Task(id, taskTitle, startDate, endDate, priority, status, taskDesc);
            task.setProjectID(projectID);


            if(statusCode == 2 && status.getCode() != 2){
                mProjectViewModel.setCompletedTasks(-1, projectID);
            }else if(statusCode != 2 && status.getCode() == 2){
                mProjectViewModel.setCompletedTasks(1, projectID);
            }

            try {
                updateData();
            } catch (ParseException e) {
                e.printStackTrace();
            }


            if (id != -1) {
                mTaskViewModel.update(task);
                Toast.makeText(this, "Update Task : " + taskTitle, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.unable_to_update,
                        Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(
                    this,
                    R.string.task_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }


    public void updateData() throws ParseException {

        tvTitle.setText(mTitle);

        tvPriority.setText(mPriority);

        switch (mPriority){
            case "low":
                tvPriority.setTextColor(Color.parseColor("#4b7bec")) ;
                priorityCode = 1;
                break;
            case "medium":
                tvPriority.setTextColor(Color.parseColor("#fa8231"));
                priorityCode = 2;
                break;
            case "high":
                tvPriority.setTextColor(Color.parseColor("#eb2f06"));
                priorityCode = 3;
                break;
        }

        switch (mStatus){
            case 0:
                tvStatus.setText("Not Started");
                break;
            case 1:
                tvStatus.setText("In Progress");
                tvStatus.setTextColor(Color.parseColor("#fa8231"));
                break;
            case 2:
                tvStatus.setText("Completed");
                tvStatus.setTextColor(Color.parseColor("#2ecc71"));
                break;
        }


        SimpleDateFormat source = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat target = new SimpleDateFormat("MMM, dd yyyy");

        if(mStartDate != null && !mStartDate.isEmpty()) {
            tvStartDate.setText(target.format(source.parse(mStartDate)));

        }


        if(mEndDate != null && !mEndDate.isEmpty())
            tvEndDate.setText(target.format(source.parse(mEndDate)));


        tvDescription.setText(mDescription);
    }

}
