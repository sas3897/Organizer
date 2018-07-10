package com.aechkae.organizer.Agile.common;

import android.provider.BaseColumns;

public class CompletedTaskTable implements BaseColumns {
    public static final String
            COL_CODE = "code",
            COL_DESC = "description",
            COL_PERC = "perc_done",
            COL_COMP_DATE = "comp_date",
            TABLE_NAME = "Completed_Tasks";

    public static String CREATE_TABLE(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CODE + " TEXT UNIQUE, " +
                COL_DESC  + " TEXT, " +
                COL_PERC + " REAL, " +
                //TODO How do you do dates in SQLite?
                COL_COMP_DATE + " INTEGER)";
    }
}