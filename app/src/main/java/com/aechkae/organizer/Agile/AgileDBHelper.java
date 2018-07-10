package com.aechkae.organizer.Agile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.aechkae.organizer.Agile.common.CompletedTaskTable;
import com.aechkae.organizer.Agile.common.UncompletedTasksTable;

public class AgileDBHelper extends SQLiteOpenHelper{

    private static final int DB_VERSION = 2;
    public static final String DB_NAME = "Agile_Database";

    public AgileDBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL(UncompletedTasksTable.CREATE_TABLE());
        sqLiteDatabase.execSQL(CompletedTaskTable.CREATE_TABLE());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //TODO overhaul this
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UncompletedTasksTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CompletedTaskTable.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}
