package com.aechkae.organizer.focus.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aechkae.organizer.focus.schemas.CompTaskTable;
import com.aechkae.organizer.focus.schemas.FocusDBAdapter;
import com.aechkae.organizer.focus.schemas.FocusDBHelper;
import com.aechkae.organizer.focus.schemas.Task;
import com.aechkae.organizer.focus.schemas.UncompTaskTable;
import com.aechkae.organizer.R;
import com.aechkae.organizer.focus.view_holders.ActiveTaskViewHolder;

import java.util.Calendar;

import static com.aechkae.organizer.focus.schemas.CompTaskTable.COL_COMP_DATE;
import static com.aechkae.organizer.focus.schemas.UncompTaskTable.COL_CODE;
import static com.aechkae.organizer.focus.schemas.UncompTaskTable.COL_DESC;

public class ActiveTaskRVAdapter extends RecyclerView.Adapter<ActiveTaskViewHolder>{
    private Context mContext;
    private Cursor mCursor;
    private FocusDBAdapter db_adapter;

    public ActiveTaskRVAdapter(Context context, Cursor cursor, FocusDBAdapter dbAdapter){
        mContext = context;
        mCursor = cursor;
        db_adapter = dbAdapter;
    }

    @Override
    public int getItemCount(){
        return mCursor.getCount();
    }

    @Override
    public void onBindViewHolder(ActiveTaskViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.bindCursor(mCursor, mContext);

        //Remove task from DB
        holder.getActiveTaskItemBinding().activeTaskDeleteBtn.setOnClickListener((view) ->
            db_adapter.deleteTask(
                    new String[]{holder.getActiveTaskItemBinding().
                            activeTaskCode.getText().toString()})
        );


        //Move to backlog
        holder.getActiveTaskItemBinding().activeMvBacklogBtn.setOnClickListener((view) ->
            db_adapter.moveTask(
                    new String[]{holder.getActiveTaskItemBinding().
                            activeTaskCode.getText().toString()}, Task.TaskType.BACKLOG)
        );

        //Complete task
        holder.getActiveTaskItemBinding().completeTaskBtn.setOnClickListener((view) -> {
            //TODO someone mentioned that you could just do "getDatabase" by some means, to
            //TODO have read and write access. important here to avoid having desc get cut off
            SQLiteDatabase taskDatabase = new FocusDBHelper(mContext).getWritableDatabase();
            //Generate a 'completed task' from the active task
            ContentValues values = new ContentValues();
            values.put(COL_CODE, holder.getActiveTaskItemBinding().activeTaskCode.getText().toString());
            values.put(COL_DESC, holder.getActiveTaskItemBinding().activeTaskDesc.getText().toString());
            values.put(COL_COMP_DATE, Calendar.getInstance().getTimeInMillis());

            //Move it to the completed task table
            taskDatabase.insert(CompTaskTable.TABLE_NAME, null, values);

            //Delete it from the uncompleted task table
            taskDatabase.delete(
                    UncompTaskTable.TABLE_NAME,
                    COL_CODE + "= ?",
                    new String[]{holder.getActiveTaskItemBinding().activeTaskCode.getText().toString()});
        });
    }

    @Override
    public ActiveTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.active_task_item, parent, false);
        return new ActiveTaskViewHolder(view);
    }


}
