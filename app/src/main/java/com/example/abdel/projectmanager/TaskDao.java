package com.example.abdel.projectmanager;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Delete
    void deleteTask(Task task);

    @Update
    void update(Task... task);


    @Query("SELECT * FROM task_table WHERE due_date >= :today AND due_date <= :week AND status != 2 ORDER BY due_date")
    LiveData<List<Task>> getUpcoming(long today, long week);

    @Query("SELECT * FROM task_table WHERE due_date <= :today AND status != 2 ORDER BY due_date")
    LiveData<List<Task>> getOverdue(long today);

    @Query("SELECT * FROM task_table WHERE task_id = :taskID")
    LiveData<Task> getTask(int taskID);

    @Query("DELETE FROM task_table WHERE task_id = :taskID")
    void deleteFromTasks(int taskID);


}
