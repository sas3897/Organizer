package com.aechkae.organizer.Agile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.aechkae.organizer.Agile.DatabaseSchemas.*;

public class AgileDBHelper extends SQLiteOpenHelper{

    private static final int DB_Version = 1;
    public static final String DB_Name = "Agile_Database";

    public AgileDBHelper(Context context){
        super(context, DB_Name, null, DB_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL(ActiveTaskTable.CREATE_TABLE());
        sqLiteDatabase.execSQL(BackburnerTaskTable.CREATE_TABLE());
        sqLiteDatabase.execSQL(BacklogTaskTable.CREATE_TABLE());
        sqLiteDatabase.execSQL(CompletedTaskTable.CREATE_TABLE());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //TODO overhaul this
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ActiveTaskTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BackburnerTaskTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BacklogTaskTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CompletedTaskTable.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}
