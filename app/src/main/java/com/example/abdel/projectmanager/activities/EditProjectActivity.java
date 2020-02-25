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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abdel.projectmanager.fragments.ProjectDatePickerFragment;
import com.example.abdel.projectmanager.R;

public class EditProjectActivity extends AppCompatActivity {

    public static final String EXTRA_NAME =
            "com.example.abdel.projectmanager.NAME";
    public static final String EXTRA_DESC =
            "com.example.abdel.projectmanager.DESC";
    public static final String EXTRA_START =
            "com.example.abdel.projectmanager.START";
    public static final String EXTRA_END =
            "com.example.abdel.projectmanager.END";



    private EditText mEditName;
    private EditText mEditDesc;
    private Button mStartDate;
    private Button mEndDate;
    private boolean start = true;

    private String startDate = "";
    private String endDate = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_project);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mEditName = findViewById(R.id.edit_name);
        mEditDesc = findViewById(R.id.edit_project_desc);
        mStartDate = findViewById(R.id.start_date);
        mEndDate = findViewById(R.id.end_date);

        final Bundle extras = getIntent().getExtras();

        if(extras != null){
            setTitle("Edit Project");
            String title = extras.getString("EXTRA_NAME", "");
            String desc = extras.getString("EXTRA_DESC", "");
            startDate = extras.getString("EXTRA_START", "");
            endDate = extras.getString("EXTRA_END", "");

            if (!title.isEmpty()) {
                mEditName.setText(title);
                mEditName.setSelection(title.length());
                mEditName.requestFocus();
            }
            if (!desc.isEmpty()) {
                mEditDesc.setText(desc);
            }
            if (!startDate.isEmpty()) {
                mStartDate.setText(startDate);
            }
            if (!endDate.isEmpty()) {
                mEndDate.setText(endDate);
            }
        }

        final Button button = findViewById(R.id.button_save_project);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditName.getText())) {
                    Toast.makeText(view.getContext(), "Please enter a project name", Toast.LENGTH_SHORT).show();
                    //setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String name = mEditName.getText().toString();
                    String desc = mEditDesc.getText().toString();
                    replyIntent.putExtra(EXTRA_NAME, name);
                    replyIntent.putExtra(EXTRA_DESC, desc);
                    replyIntent.putExtra(EXTRA_START, startDate);
                    replyIntent.putExtra(EXTRA_END, endDate);
                    if (extras != null && extras.containsKey("EXTRA_ID")) {
                        int id = extras.getInt("EXTRA_ID", -1);
                        if (id != -1) {
                            replyIntent.putExtra("EXTRA_ID", id);
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

    public void showDatePickerDialogStart(View v) {
        DialogFragment newFragment = new ProjectDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
        start = true;
    }

    public void showDatePickerDialogEnd(View v) {
        DialogFragment newFragment = new ProjectDatePickerFragment();
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
