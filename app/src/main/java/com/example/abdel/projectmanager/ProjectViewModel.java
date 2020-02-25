package com.example.abdel.projectmanager;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class ProjectViewModel extends AndroidViewModel {

    //Fields
    private ProjectRepository mRepository;

    private LiveData<List<Project>> mAllProjects;
    private LiveData<ProjectAndAllTasks> mAllTasks;

    //Constructor
    public ProjectViewModel (Application application) {
        super(application);
        mRepository = new ProjectRepository(application);
        mAllProjects = mRepository.getAllProjects();
    }


    public LiveData<List<Project>> getAllProjects() {
        return mAllProjects;
    }


    public LiveData<ProjectAndAllTasks> getAllTasks(int projectID) {
        mAllTasks = mRepository.getAllTasks(projectID);
        return mAllTasks;
    }


    public void insert(Project project) {
        mRepository.insert(project);
    }


    public void update(Project project) {
        mRepository.update(project);
    }


    public void deleteProject(int projectID) {mRepository.deleteProject(projectID);}



    public void setTotalTasks(int totalTasks, int projectID){mRepository.setTotalTasks(totalTasks, projectID);}

    public void setCompletedTasks(int completedTasks, int projectID){mRepository.setCompletedTasks(completedTasks, projectID);}


}
