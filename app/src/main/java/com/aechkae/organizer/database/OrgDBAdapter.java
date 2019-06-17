package com.aechkae.organizer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.aechkae.organizer.R;
import com.aechkae.organizer.database.schemas.CompTaskTable;
import com.aechkae.organizer.database.schemas.UncompTaskTable;
import com.aechkae.organizer.focus.UncompTask;
import com.aechkae.organizer.focus.CompTask;
import com.aechkae.organizer.focus.Task.TaskType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.aechkae.organizer.database.schemas.CompTaskTable.COL_COMP_DATE;
import static com.aechkae.organizer.database.schemas.CompTaskTable.COL_DESC;
import static com.aechkae.organizer.database.schemas.UncompTaskTable.COL_CODE;
import static com.aechkae.organizer.database.schemas.UncompTaskTable.COL_PERC;
import static com.aechkae.organizer.database.schemas.UncompTaskTable.COL_TYPE;


public class OrgDBAdapter {
    private Context context;
    private SQLiteDatabase db;
    private OrgDBHelper dbHelper;

    public OrgDBAdapter(Context c){
        context = c;
        dbHelper = new OrgDBHelper(context);
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

    public List<UncompTask> getAllUncompTaskOfType(TaskType ty){
        String[] proj = {
                UncompTaskTable.COL_CODE,
                UncompTaskTable.COL_DESC,
                UncompTaskTable.COL_PERC
        };

        String selectClause = UncompTaskTable.COL_TYPE + " = ?";
        String [] selectArgs = {ty.getDb_flag() + ""};

        Cursor uncompTasksCur = db.query(UncompTaskTable.TABLE_NAME, proj, selectClause,
                selectArgs, null, null, null);

        List<UncompTask> tasks = new ArrayList<>();
        if (uncompTasksCur != null && uncompTasksCur.moveToFirst()) {

            while (!uncompTasksCur.isAfterLast()) {
                String idCode = uncompTasksCur.getString(
                        uncompTasksCur.getColumnIndexOrThrow(COL_CODE));
                String desc = uncompTasksCur.getString(
                        uncompTasksCur.getColumnIndexOrThrow(UncompTaskTable.COL_DESC));

                // TODO not sure if the context here is correct or needs to be updated...
                String perc_done = context.getString(R.string.focus_perc_done_format,
                        uncompTasksCur.getFloat(uncompTasksCur.getColumnIndexOrThrow(COL_PERC)));

                UncompTask task = new UncompTask(idCode, desc, perc_done);
                tasks.add(task);
                uncompTasksCur.moveToNext();
            }
            uncompTasksCur.close();
        }

        return tasks;
    }

    public List<CompTask> getAllCompTask(){
        String[] proj = {
                CompTaskTable.COL_CODE,
                CompTaskTable.COL_DESC,
                CompTaskTable.COL_COMP_DATE
        };
        String orderbyClause = CompTaskTable.COL_COMP_DATE + " DESC";

        Cursor compTasksCur = db.query(CompTaskTable.TABLE_NAME, proj, null,
                null, null, null, orderbyClause);

        List<CompTask> tasks = new ArrayList<>();

        if (compTasksCur != null && compTasksCur.moveToFirst()) {

            while (!compTasksCur.isAfterLast()) {
                String idCode = compTasksCur.getString(
                        compTasksCur.getColumnIndexOrThrow(CompTaskTable.COL_CODE));
                String desc = compTasksCur.getString(
                        compTasksCur.getColumnIndexOrThrow(COL_DESC));

                Calendar cal  = Calendar.getInstance();
                cal.setTimeInMillis(compTasksCur.getLong(compTasksCur.getColumnIndexOrThrow(COL_COMP_DATE)));
                String comp_date = new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(cal.getTime());

                CompTask task = new CompTask(idCode, desc, comp_date);
                tasks.add(task);
                compTasksCur.moveToNext();
            }
            compTasksCur.close();
        }

        return tasks;
    }

    public void deleteTask(String[] taskCode){
        db.delete(
                UncompTaskTable.TABLE_NAME,
                COL_CODE + "= ?",
                taskCode);
    }

    public void moveTask(String[] taskCode, TaskType ty){
        ContentValues values = new ContentValues();
        values.put(COL_TYPE, ty.getDb_flag());
        db.update(
                UncompTaskTable.TABLE_NAME,
                values,
                COL_CODE + "= ?",
                taskCode);
    }

    public void completeTask(String taskCode, String taskDesc){
        db.beginTransaction();

        try{
            db.delete(UncompTaskTable.TABLE_NAME,
                    COL_CODE + "= ?",
                    new String[]{taskCode});

            ContentValues values = new ContentValues();
            values.put(COL_CODE, taskCode);
            values.put(UncompTaskTable.COL_DESC, taskDesc);
            values.put(COL_COMP_DATE, Calendar.getInstance().getTimeInMillis());

            //Move it to the completed task table
            db.insert(CompTaskTable.TABLE_NAME, null, values);

            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }
}
