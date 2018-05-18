package com.aechkae.organizer.Agile;

import android.provider.BaseColumns;

/**
 * All the schemas for the various tables I have to draw from
 */
abstract class DatabaseSchemas implements BaseColumns {
    static final String
            COL_CODE = "code",
            COL_DESC = "description",
            COL_PERC = "perc_done";
}

class ActiveTaskTable extends DatabaseSchemas{
    static final String TABLE_NAME = "Active_Tasks";

    public static String CREATE_TABLE(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CODE + " TEXT, " +
                COL_DESC  + " TEXT, " +
                COL_PERC + " REAL)";
    }
}

class BackburnerTaskTable extends DatabaseSchemas{
    static final String TABLE_NAME = "Backburner_Tasks";

    public static String CREATE_TABLE(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CODE + " TEXT, " +
                COL_DESC  + " TEXT, " +
                COL_PERC + " REAL)";
    }
}

class BacklogTaskTable extends DatabaseSchemas{
    static final String TABLE_NAME = "Backlog_Tasks";

    public static String CREATE_TABLE(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CODE + " TEXT, " +
                COL_DESC  + " TEXT, " +
                COL_PERC + " REAL)";
    }
}

class CompletedTaskTable extends DatabaseSchemas{
    static final String TABLE_NAME = "Completed_Tasks";
    private static final String COL_COMP_DATE = "comp_date";

    public static String CREATE_TABLE(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CODE + " TEXT, " +
                COL_DESC  + " TEXT, " +
                COL_PERC + " REAL, " +
                //TODO How do you do dates in SQLite?
                COL_COMP_DATE + " TEXT)";
    }
}
