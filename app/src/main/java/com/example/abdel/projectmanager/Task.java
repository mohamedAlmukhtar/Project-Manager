package com.example.abdel.projectmanager;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(indices = {@Index("projectID")}, tableName = "task_table", foreignKeys =
@ForeignKey(entity = Project.class, parentColumns = "pro_id", childColumns = "projectID", onDelete = CASCADE))
public class Task {


    //------------------------------------------Constructors--------------------------------------//

    public Task(@NonNull String title, Date startDate, Date dueDate, Priority priority, Status status, String description) {
        this.title = title;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
        this.description = description;
    }

    @Ignore
    public Task(int id, @NonNull String title, Date startDate, Date dueDate, Priority priority, Status status, String description) {
        this.ID = id;
        this.title = title;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
        this.description = description;
    }


    //-----------------------------------------Columns----------------------------------//

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "task_id")
    private int ID;
    private int projectID;


    @NonNull
    @ColumnInfo(name = "task_title")
    private String title;


    @ColumnInfo(name = "start_date")
    @TypeConverters(DateConverter.class)
    private Date startDate;


    @ColumnInfo(name = "due_date")
    @TypeConverters(DateConverter.class)
    private Date dueDate;


    @TypeConverters(PriorityConverter.class)
    private Priority priority;


    @TypeConverters(StatusConverter.class)
    private Status status;


    @ColumnInfo()
    private String description;


    public enum Priority{
        NONE(0),
        LOW(1),
        MEDIUM(2),
        HIGH(3);

        private int code;

        Priority(int code){
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }


    public enum Status{
        NOT_STARTED(0),
        IN_PROGRESS(1),
        COMPLETED(2);

        private int code;

        Status(int code) {
            this.code = code;
        }


        public int getCode() {
            return code;
        }
    }



    //---------------------------------------------Setters & Getters---------------------------------------//


    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public void setTitle(String title) {
        this.title = title;
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

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }


}
