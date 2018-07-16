package com.aechkae.organizer.Focus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aechkae.organizer.Focus.schemas.TaskType;
import com.aechkae.organizer.Focus.schemas.UncompletedTasksTable;
import com.aechkae.organizer.R;
import com.aechkae.organizer.databinding.ActiveTaskItemBinding;

import static com.aechkae.organizer.Focus.schemas.Task.CUTOFF_LENGTH;
import static com.aechkae.organizer.Focus.schemas.UncompletedTasksTable.COL_CODE;
import static com.aechkae.organizer.Focus.schemas.UncompletedTasksTable.COL_DESC;
import static com.aechkae.organizer.Focus.schemas.UncompletedTasksTable.COL_PERC;
import static com.aechkae.organizer.Focus.schemas.UncompletedTasksTable.COL_TYPE;
import static com.aechkae.organizer.Focus.schemas.UncompletedTasksTable.TABLE_NAME;

public class ActiveTaskRecyclerViewAdapter extends RecyclerView.Adapter<ActiveTaskRecyclerViewAdapter.ActiveTaskViewHolder>{
    private Context mContext;
    private Cursor mCursor;

    ActiveTaskRecyclerViewAdapter(Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }

    @Override
    public int getItemCount(){
        return mCursor.getCount();
    }

    @Override
    public void onBindViewHolder(ActiveTaskViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.bindCursor(mCursor);

        //Remove task from DB
        holder.activeTaskItemBinding.activeTaskDeleteBtn.setOnClickListener((view) -> {
            SQLiteDatabase taskDatabase = new FocusDBHelper(mContext).getWritableDatabase();
            taskDatabase.delete(
                    TABLE_NAME,
                    COL_CODE + "= ?",
                    new String[]{holder.activeTaskItemBinding.activeTaskCode.getText().toString()});
        });

        //Move to backlog
        holder.activeTaskItemBinding.activeMvBacklogBtn.setOnClickListener((view) -> {
            SQLiteDatabase taskDatabase = new FocusDBHelper(mContext).getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_TYPE, TaskType.BACKLOG);
            taskDatabase.update(
                    TABLE_NAME,
                    values,
                    COL_CODE + "= ?",
                    new String[]{holder.activeTaskItemBinding.activeTaskCode.getText().toString()});
        });
    }

    @Override
    public ActiveTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.active_task_item, parent, false);
        return new ActiveTaskViewHolder(view);
    }

    class ActiveTaskViewHolder extends RecyclerView.ViewHolder{
        ActiveTaskItemBinding activeTaskItemBinding;
        ActiveTaskViewHolder(View itemView){
            super(itemView);
            activeTaskItemBinding = DataBindingUtil.bind(itemView);
        }
        void bindCursor(Cursor cursor){
            activeTaskItemBinding.activeTaskCode.setText(cursor.getString(
                    cursor.getColumnIndexOrThrow(COL_CODE)));

            String desc = cursor.getString(
                    cursor.getColumnIndexOrThrow(COL_DESC));
            if(desc.length() > CUTOFF_LENGTH){
                activeTaskItemBinding.activeTaskDesc.setText(desc.substring(0, CUTOFF_LENGTH));
            }
            else {
                activeTaskItemBinding.activeTaskDesc.setText(desc);
            }
            activeTaskItemBinding.activeTaskPercDone.setText("" + cursor.getDouble(
                    cursor.getColumnIndexOrThrow(COL_PERC)));
        }
    }
}
