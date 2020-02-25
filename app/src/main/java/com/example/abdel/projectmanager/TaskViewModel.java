package com.example.abdel.projectmanager;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class TaskViewModel extends AndroidViewModel {


    //fields
    private TaskRepository mRepository;


    //Constructor
    public TaskViewModel(Application application) {
        super(application);
        mRepository = new TaskRepository(application);
    }


    public LiveData<List<Task>> getUpcoming(long today, long week) {
        return mRepository.getUpcoming(today, week);

    }

    public LiveData<List<Task>> getOverdue(long today) {
        return mRepository.getOverdue(today);

    }


    public void insert(Task task) {
        mRepository.insert(task);
    }

    public void deleteTask(Task task) {mRepository.deleteTask(task);}

    public void deleteFromTasks(int taskID) {mRepository.deleteFromTasks(taskID);}

    public void update(Task task) {
        mRepository.update(task);
    }

}
