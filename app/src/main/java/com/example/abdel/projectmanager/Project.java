package com.example.abdel.projectmanager;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "project_table")
public class Project {


    //---------------------------------------Columns---------------------------------//

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "pro_id")
    private int ID;

    @NonNull
    @ColumnInfo(name = "pro_name")
    private String name;

    @ColumnInfo(name = "pro_desc")
    private String description;

    @ColumnInfo(name = "start_date")
    @TypeConverters(DateConverter.class)
    private Date startDate;

    @ColumnInfo(name = "due_date")
    @TypeConverters(DateConverter.class)
    private Date dueDate;

    @ColumnInfo(name = "total_tasks")
    private int totalTasks = 0;

    @ColumnInfo(name = "completed_tasks")
    private int completedTasks = 0;


    //--------------------------------------Constructors----------------------------------//

    public Project(@NonNull String name, String description, Date startDate, Date dueDate, int totalTasks, int completedTasks){
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
    }

    @Ignore
    public Project(int id, @NonNull String name, String description, Date startDate, Date dueDate, int totalTasks, int completedTasks){
        this.ID = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
    }


    //----------------------------------Setters & Getters---------------------------------//

    public void setID(@NonNull int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public int getCompletedTasks() {
        return completedTasks;
    }


    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
