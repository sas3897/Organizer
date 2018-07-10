package com.aechkae.organizer.Agile.common;

import android.provider.BaseColumns;

/**
 * All the schemas for the various tables I have to draw from
 */
public class UncompletedTasksTable implements BaseColumns {
    public static final String
            COL_CODE = "code",
            COL_DESC = "description",
            COL_PERC = "perc_done",
            COL_TYPE = "task_type",
            TABLE_NAME = "Uncompleted_Tasks";

    public static String CREATE_TABLE(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CODE + " TEXT UNIQUE, " +
                COL_DESC + " TEXT, " +
                COL_PERC + " REAL, " +
                COL_TYPE + " INTEGER)";
    }
}
