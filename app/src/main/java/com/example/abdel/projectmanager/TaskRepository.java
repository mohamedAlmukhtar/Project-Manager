package com.example.abdel.projectmanager;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class TaskRepository {

    //Fields
    private TaskDao mTaskDao;
    private LiveData<Task> mTask;


    //Constructor
    TaskRepository(Application application) {
        ProjectRoomDatabase db = ProjectRoomDatabase.getDatabase(application);
        mTaskDao = db.taskDao();

    }

    LiveData<Task> getTask(int taskID) {
        mTask = mTaskDao.getTask(taskID);
        return mTask;
    }

    LiveData<List<Task>> getUpcoming(long today, long week) {
        return mTaskDao.getUpcoming(today, week);

    }

    LiveData<List<Task>> getOverdue(long today) {
        return mTaskDao.getOverdue(today);

    }

    public void insert (Task task) {
        new insertAsyncTask(mTaskDao).execute(task);
    }

    public void deleteTask(Task task)  {
        new deleteAsyncTask(mTaskDao).execute(task);
    }

    public void deleteFromTasks(int taskID)  {
        new TaskRepository.deleteFromAsyncTask(mTaskDao).execute(taskID);
    }

    public void update(Task task)  {
        new updateAsyncTask(mTaskDao).execute(task);
    }



    //--------------------------------------------Async Tasks------------------------------------------//

    //insert
    private static class insertAsyncTask extends AsyncTask<Task, Void, Void> {

        private TaskDao mAsyncTaskDao;

        insertAsyncTask(TaskDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Task... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }


    //delete
    private static class deleteAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mAsyncTaskDao;

        deleteAsyncTask(TaskDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Task... params) {
            mAsyncTaskDao.deleteTask(params[0]);
            return null;
        }
    }

    private static class deleteFromAsyncTask extends AsyncTask<Integer, Void, Void> {
        private TaskDao mAsyncTaskDao;

        deleteFromAsyncTask(TaskDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.deleteFromTasks(params[0]);
            return null;
        }
    }

    //update
    private static class updateAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mAsyncTaskDao;

        updateAsyncTask(TaskDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Task... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

}
