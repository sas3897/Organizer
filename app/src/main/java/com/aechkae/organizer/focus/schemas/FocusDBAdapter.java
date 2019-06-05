package com.aechkae.organizer.focus.schemas;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.aechkae.organizer.focus.schemas.Task.TaskType;


public class FocusDBAdapter {
    private Context context;
    private SQLiteDatabase db;
    private FocusDBHelper dbHelper;

    public FocusDBAdapter(Context c){
        context = c;
        dbHelper = new FocusDBHelper(context);
    }

    public void openDB(){
        try{
            db=dbHelper.getWritableDatabase();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void closeDB(){
        try{
            dbHelper.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Cursor getAllUncompTaskOfType(TaskType ty){
        String[] proj = {
                UncompTaskTable.COL_CODE,
                UncompTaskTable.COL_DESC,
                UncompTaskTable.COL_PERC
        };

        String selectClause = UncompTaskTable.COL_TYPE + " = ?";
        String [] selectArgs = {ty.getDb_flag() + ""};

        return db.query(UncompTaskTable.TABLE_NAME, proj, selectClause,
                selectArgs, null, null, null);
    }

    public Cursor getAllCompTask(){
        String[] proj = {
                CompTaskTable.COL_CODE,
                CompTaskTable.COL_DESC,
                CompTaskTable.COL_COMP_DATE
        };
        String orderbyClause = CompTaskTable.COL_COMP_DATE + " DESC";

        return db.query(CompTaskTable.TABLE_NAME, proj, null,
                null, null, null, orderbyClause);
    }
}
