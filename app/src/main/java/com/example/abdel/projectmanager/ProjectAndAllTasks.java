package com.example.abdel.projectmanager;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class ProjectAndAllTasks {

    @Embedded
    public Project project;

    @Relation(parentColumn = "pro_id", entityColumn = "projectID", entity = Task.class)
    private List<Task> tasks;

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

}
