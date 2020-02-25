package com.example.abdel.projectmanager;

import android.arch.persistence.room.TypeConverter;

import static com.example.abdel.projectmanager.Task.Priority.HIGH;
import static com.example.abdel.projectmanager.Task.Priority.LOW;
import static com.example.abdel.projectmanager.Task.Priority.MEDIUM;
import static com.example.abdel.projectmanager.Task.Priority.NONE;

public class PriorityConverter {

    @TypeConverter
    public static Task.Priority toPriority(int priority) {
        if (priority == NONE.getCode()){
            return NONE;
        }
        else if (priority == LOW.getCode()) {
            return LOW;
        } else if (priority == MEDIUM.getCode()) {
            return MEDIUM;
        } else if (priority == HIGH.getCode()) {
            return HIGH;
        } else {
            throw new IllegalArgumentException("Could not recognize priority");
        }
    }

    @TypeConverter
    public static int toInteger(Task.Priority priority) {
        return priority.getCode();
    }
}
