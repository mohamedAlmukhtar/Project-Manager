package com.example.abdel.projectmanager.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.abdel.projectmanager.R;
import com.example.abdel.projectmanager.fragments.TaskDatePickerFragment;

import static com.example.abdel.projectmanager.activities.ProjectActivity.EXTRA_DATA_ID;
import static com.example.abdel.projectmanager.activities.ProjectActivity.EXTRA_DATA_UPDATE_TITLE;
import static com.example.abdel.projectmanager.activities.ProjectActivity.EXTRA_DATA_UPDATE_DESC;
import static com.example.abdel.projectmanager.activities.ProjectActivity.EXTRA_DATA_UPDATE_PRIORITY;
import static com.example.abdel.projectmanager.activities.ProjectActivity.EXTRA_DATA_UPDATE_STATUS;
import static com.example.abdel.projectmanager.activities.ProjectActivity.EXTRA_DATA_UPDATE_START;
import static com.example.abdel.projectmanager.activities.ProjectActivity.EXTRA_DATA_UPDATE_END;

public class EditTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String EXTRA_TITLE =
            "com.example.abdel.projectmanager.TITLE";
    public static final String EXTRA_DESC =
            "com.example.abdel.projectmanager.DESC";
    public static final String EXTRA_PRIORITY =
            "com.example.abdel.projectmanager.PRIORITY";
    public static final String EXTRA_STATUS =
            "com.example.abdel.projectmanager.STATUS";
    public static final String EXTRA_REPLY_ID =
            "com.example.abdel.projectmanager.REPLY_ID";
    public static final String EXTRA_START =
            "com.example.abdel.projectmanager.START";
    public static final String EXTRA_END =
            "com.example.abdel.projectmanager.END";


    private EditText mEditTitle;
    private EditText mEditDesc;
    private Spinner mPrioritySpinner;
    private Spinner mStatusSpinner;
    private Button mStartDate;
    private Button mEndDate;


    private String priority = "LOW";
    private String status = "COMPLETED";

    private boolean start = true;
    private String startDate = "";
    private String endDate = "";

    public int statusCode;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mEditTitle = findViewById(R.id.edit_title);
        mEditDesc = findViewById(R.id.edit_task_desc);
        mStartDate = findViewById(R.id.start_date);
        mEndDate = findViewById(R.id.end_date);

        mPrioritySpinner = (Spinner) findViewById(R.id.priority_spinner);
        mStatusSpinner = (Spinner) findViewById(R.id.status_spinner);

        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(this,
                R.array.priority_array, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);

        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mPrioritySpinner.setAdapter(priorityAdapter);
        mStatusSpinner.setAdapter(statusAdapter);

        mPrioritySpinner.setOnItemSelectedListener(this);
        mStatusSpinner.setOnItemSelectedListener(this);

        int id = -1 ;

        final Bundle extras = getIntent().getExtras();

        // If we are passed content, fill it in for the user to edit.
        if (extras != null) {
            setTitle("Edit Task");
            String title = extras.getString(EXTRA_DATA_UPDATE_TITLE, "");
            String desc = extras.getString(EXTRA_DATA_UPDATE_DESC, "");
            int priorityCode = extras.getInt(EXTRA_DATA_UPDATE_PRIORITY, -1);
            statusCode = extras.getInt(EXTRA_DATA_UPDATE_STATUS, -1);
            startDate = extras.getString(EXTRA_DATA_UPDATE_START, "");
            endDate = extras.getString(EXTRA_DATA_UPDATE_END, "");
            if (!title.isEmpty()) {
                mEditTitle.setText(title);
                mEditTitle.setSelection(title.length());
                mEditTitle.requestFocus();
            }
            if (!desc.isEmpty()) {
                mEditDesc.setText(desc);
            }
            if (priorityCode != -1) {
                mPrioritySpinner.setSelection(priorityCode);
            }
            if (statusCode != -1) {
                mStatusSpinner.setSelection(statusCode);
            }
            if (!startDate.isEmpty()) {
                mStartDate.setText(startDate);
            }
            if (!endDate.isEmpty()) {
                mEndDate.setText(endDate);
            }
        } // Otherwise, start with empty fields.

        final Button button = findViewById(R.id.button_save_task);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditTitle.getText())) {
                    Toast.makeText(view.getContext(), "Please enter a task title", Toast.LENGTH_SHORT).show();
                    //setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String title = mEditTitle.getText().toString();
                    String desc = mEditDesc.getText().toString();
                    replyIntent.putExtra(EXTRA_TITLE, title);
                    replyIntent.putExtra(EXTRA_DESC, desc);
                    replyIntent.putExtra(EXTRA_PRIORITY, priority);
                    replyIntent.putExtra(EXTRA_STATUS, status);
                    replyIntent.putExtra(EXTRA_START, startDate);
                    replyIntent.putExtra(EXTRA_END, endDate);
                    replyIntent.putExtra("STATUS_CHANGE", statusCode);
                    if (extras != null && extras.containsKey(EXTRA_DATA_ID)) {
                        int id = extras.getInt(EXTRA_DATA_ID, -1);
                        if (id != -1) {
                            replyIntent.putExtra(EXTRA_REPLY_ID, id);
                        }
                    }
                    setResult(RESULT_OK, replyIntent);
                    finish();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {

        showAlert();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                showAlert();
                break;
        }

        return true;
    }

    public void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit without saving changes")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Object selected = parent.getItemAtPosition(position);

        switch (parent.getId()){
            case R.id.priority_spinner:
                priority = selected.toString().toUpperCase();
                break;
            case R.id.status_spinner:
                switch (selected.toString()){
                    case "Not Started":
                        status = "NOT_STARTED";
                        break;
                    case "In Progress":
                        status = "IN_PROGRESS";
                        break;
                    default:
                        //Completed case
                        status = selected.toString().toUpperCase();
                }
        }



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void showDatePickerDialogStart(View v) {
        DialogFragment newFragment = new TaskDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
        start = true;
    }

    public void showDatePickerDialogEnd(View v) {
        DialogFragment newFragment = new TaskDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
        start = false;
    }

    public void processDatePickerResult(int year, int month, int day) {
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);

        String date = (day_string + "/" + month_string + "/" + year_string);

        if (start){
            mStartDate.setText(date);
            startDate = date;
        }
        else{
            mEndDate.setText(date);
            endDate = date;
        }

    }
}
