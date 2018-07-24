package com.aechkae.organizer.focus.schemas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FocusDBHelper extends SQLiteOpenHelper{

    private static final int DB_VERSION = 4;
    private static final String DB_NAME = "Organizer_Database";

    public FocusDBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL(UncompletedTasksTable.CREATE_TABLE());
        sqLiteDatabase.execSQL(UncompletedTasksTable.CREATE_INDEX());

        sqLiteDatabase.execSQL(CompletedTaskTable.CREATE_TABLE());
        sqLiteDatabase.execSQL(CompletedTaskTable.CREATE_INDEX());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UncompletedTasksTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CompletedTaskTable.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}
