package com.aechkae.organizer.focus.schemas;

import android.provider.BaseColumns;

/** Essentially just for the SQL statement, but in a centralized location*/
public final class UncompletedTasksTable implements BaseColumns {
    public static final String
            COL_CODE = "code",
            COL_DESC = "description",
            COL_PERC = "perc_done",
            COL_TYPE = "task_type",
            TABLE_NAME = "Uncompleted_Tasks";

    private static final String INDEX_NAME = "uncompT_index";

    public static String CREATE_TABLE(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CODE + " TEXT UNIQUE, " +
                COL_DESC + " TEXT, " +
                COL_PERC + " REAL, " +
                COL_TYPE + " INTEGER)";
    }

    public static String CREATE_INDEX(){
        return "CREATE UNIQUE INDEX IF NOT EXISTS " + INDEX_NAME + " ON " + TABLE_NAME + " (" + COL_CODE + ")";
    }

    //No initializing this class!
    private UncompletedTasksTable(){}
}
