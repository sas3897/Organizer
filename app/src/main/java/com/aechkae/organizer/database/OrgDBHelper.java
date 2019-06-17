package com.aechkae.organizer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.aechkae.organizer.database.schemas.CompTaskTable;
import com.aechkae.organizer.database.schemas.UncompTaskTable;


public class OrgDBHelper extends SQLiteOpenHelper{

    private static final int DB_VERSION = 5;
    private static final String DB_NAME = "Organizer_Database";

    public OrgDBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL(UncompTaskTable.CREATE_TABLE());
        sqLiteDatabase.execSQL(UncompTaskTable.CREATE_INDEX());

        sqLiteDatabase.execSQL(CompTaskTable.CREATE_TABLE());
        sqLiteDatabase.execSQL(CompTaskTable.CREATE_INDEX());
    }

    @Override
    public void onUpgrade(SQLiteDatabase taskDB, int oldVer, int newVer) {
        taskDB.execSQL("DROP TABLE IF EXISTS " + UncompTaskTable.TABLE_NAME);
        taskDB.execSQL("DROP TABLE IF EXISTS " + CompTaskTable.TABLE_NAME);

        onCreate(taskDB);
    }
}
