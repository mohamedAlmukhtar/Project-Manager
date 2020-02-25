package com.example.abdel.projectmanager;

import android.arch.persistence.room.TypeConverter;

import static com.example.abdel.projectmanager.Task.Status.COMPLETED;
import static com.example.abdel.projectmanager.Task.Status.IN_PROGRESS;
import static com.example.abdel.projectmanager.Task.Status.NOT_STARTED;

public class StatusConverter {

    @TypeConverter
    public static Task.Status toStatus(int status) {
        if (status == NOT_STARTED.getCode()) {
            return NOT_STARTED;
        } else if (status == IN_PROGRESS.getCode()) {
            return IN_PROGRESS;
        } else if (status == COMPLETED.getCode()) {
            return COMPLETED;
        } else {
            throw new IllegalArgumentException("Could not recognize status");
        }
    }

    @TypeConverter
    public static int toInteger(Task.Status status) {
        return status.getCode();
    }
}
