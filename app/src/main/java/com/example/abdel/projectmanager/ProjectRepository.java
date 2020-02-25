package com.example.abdel.projectmanager;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class ProjectRepository {

    //Fields
    private ProjectDao mProjectDao;
    private LiveData<List<Project>> mAllProjects;
    private LiveData<ProjectAndAllTasks> mAllTasks;


    //Constructor
    ProjectRepository(Application application) {
        ProjectRoomDatabase db = ProjectRoomDatabase.getDatabase(application);
        mProjectDao = db.projectDao();
        mAllProjects = mProjectDao.getAllProjects();

    }


    LiveData<List<Project>> getAllProjects() {
        return mAllProjects;
    }


    LiveData<ProjectAndAllTasks> getAllTasks(int projectID) {
        mAllTasks = mProjectDao.getAllTasks(projectID);
        return mAllTasks;
    }

    public void insert (Project project) {
        new insertAsyncTask(mProjectDao).execute(project);
    }


    public void update(Project project)  {
        new updateAsyncTask(mProjectDao).execute(project);
    }


    public void deleteProject(int projectID)  {
        new ProjectRepository.deleteAsyncTask(mProjectDao).execute(projectID);
    }


    public void setTotalTasks(int totalTasks, int projectID){
        new ProjectRepository.totalAsyncTask(mProjectDao).execute(totalTasks, projectID);
    }


    public void setCompletedTasks(int completedTasks, int projectID){
        new ProjectRepository.completedAsyncTask(mProjectDao).execute(completedTasks, projectID);
    }


    //-------------------------------Async Tasks------------------------------------------//

    //------------Insert-----------
    private static class insertAsyncTask extends AsyncTask<Project, Void, Void> {

        private ProjectDao mAsyncTaskDao;

        insertAsyncTask(ProjectDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Project... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    //-----------update-------------
    private static class updateAsyncTask extends AsyncTask<Project, Void, Void> {
        private ProjectDao mAsyncTaskDao;

        updateAsyncTask(ProjectDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Project... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    //----------delete-------------
    private static class deleteAsyncTask extends AsyncTask<Integer, Void, Void> {
        private ProjectDao mAsyncTaskDao;

        deleteAsyncTask(ProjectDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.deleteProject(params[0]);
            return null;
        }
    }


    //-----------total Tasks---------
    private static class totalAsyncTask extends AsyncTask<Integer, Void, Void> {
        private ProjectDao mAsyncTaskDao;

        totalAsyncTask(ProjectDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.setTotalTasks(params[0], params[1]);
            return null;
        }
    }

    //-----------Completed Tasks---------
    private static class completedAsyncTask extends AsyncTask<Integer, Void, Void> {
        private ProjectDao mAsyncTaskDao;

        completedAsyncTask(ProjectDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.setCompletedTasks(params[0], params[1]);
            return null;
        }
    }


}
