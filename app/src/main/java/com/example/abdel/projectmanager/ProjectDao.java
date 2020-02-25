package com.example.abdel.projectmanager;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ProjectDao {

    @Insert
    void insert(Project project);

    @Update
    void update(Project... project);


    @Query("DELETE FROM project_table WHERE pro_id = :projectID")
    void deleteProject(int projectID);

    @Query("DELETE FROM project_table")
    void deleteAll();


    @Query("UPDATE project_table SET total_tasks = total_tasks + :totalTasks WHERE pro_id = :projectID")
    void setTotalTasks(int totalTasks, int projectID);


    @Query("UPDATE project_table SET completed_tasks = completed_tasks + :completedTasks WHERE pro_id = :projectID")
    void setCompletedTasks(int completedTasks, int projectID);


    @Query("SELECT * from project_table ORDER BY pro_id DESC")
    LiveData<List<Project>> getAllProjects();


    @Transaction
    @Query("SELECT * from project_table WHERE pro_id = :projectID")
    LiveData<ProjectAndAllTasks> getAllTasks(int projectID);

}
