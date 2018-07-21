package com.aechkae.organizer.Focus.schemas;

import android.provider.BaseColumns;

/** Essentially just for the SQL statement, but in a centralized location*/
public final class CompletedTaskTable implements BaseColumns {
    public static final String
            COL_CODE = "code",
            COL_DESC = "description",
            COL_COMP_DATE = "comp_date",
            TABLE_NAME = "Completed_Tasks";

    private static final String INDEX_NAME = "compT_index";

    public static String CREATE_TABLE(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CODE + " TEXT UNIQUE, " +
                COL_DESC  + " TEXT, " +
                COL_COMP_DATE + " INTEGER)";
    }

    public static String CREATE_INDEX(){
        return "CREATE UNIQUE INDEX IF NOT EXISTS " + INDEX_NAME + " ON " + TABLE_NAME +
                " (" + COL_COMP_DATE + ", " +
                COL_CODE + ")";
    }

    //No initializing this class!
    private CompletedTaskTable(){}
}
