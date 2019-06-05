package com.aechkae.organizer.focus.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aechkae.organizer.focus.schemas.FocusDBHelper;
import com.aechkae.organizer.focus.schemas.Task;
import com.aechkae.organizer.focus.schemas.UncompTaskTable;
import com.aechkae.organizer.R;
import com.aechkae.organizer.databinding.BacklogTaskItemBinding;

import static com.aechkae.organizer.focus.schemas.Task.UNCOMP_DESC_CUTOFF_LENGTH;
import static com.aechkae.organizer.focus.schemas.UncompTaskTable.COL_CODE;
import static com.aechkae.organizer.focus.schemas.UncompTaskTable.COL_DESC;
import static com.aechkae.organizer.focus.schemas.UncompTaskTable.COL_PERC;
import static com.aechkae.organizer.focus.schemas.UncompTaskTable.COL_TYPE;
import static com.aechkae.organizer.focus.schemas.UncompTaskTable.TABLE_NAME;

public class BacklogTaskRVAdapter extends RecyclerView.Adapter<BacklogTaskRVAdapter.BacklogTaskViewHolder>{
    private Context mContext;
    private Cursor mCursor;

    public BacklogTaskRVAdapter(Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }

    @Override
    public int getItemCount(){
        return mCursor.getCount();
    }

    @Override
    public void onBindViewHolder(BacklogTaskViewHolder holder, int position){
        mCursor.moveToPosition(position);
        holder.bindCursor(mCursor);

        //Remove task from DB
        holder.backlogTaskBinding.backlogDeleteBtn.setOnClickListener((view) -> {
            SQLiteDatabase taskDatabase = new FocusDBHelper(mContext).getWritableDatabase();
            taskDatabase.delete(
                    UncompTaskTable.TABLE_NAME,
                    COL_CODE + "= ?",
                    new String[]{holder.backlogTaskBinding.taskCode.getText().toString()});
        });

        //Move to active
        holder.backlogTaskBinding.backlogMvActiveBtn.setOnClickListener((view) -> {
            SQLiteDatabase taskDatabase = new FocusDBHelper(mContext).getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_TYPE, Task.TaskType.ACTIVE.getDb_flag());
            taskDatabase.update(
                    TABLE_NAME,
                    values,
                    COL_CODE + "= ?",
                    new String[]{holder.backlogTaskBinding.taskCode.getText().toString()});
        });
    }

    @Override
    public BacklogTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.backlog_task_item, parent, false);
        return new BacklogTaskViewHolder(view);
    }

    class BacklogTaskViewHolder extends RecyclerView.ViewHolder{
        BacklogTaskItemBinding backlogTaskBinding;

        BacklogTaskViewHolder(View itemView){
            super(itemView);
            backlogTaskBinding = DataBindingUtil.bind(itemView);
        }

        void bindCursor(Cursor cursor){
            backlogTaskBinding.taskCode.setText(cursor.getString(
                    cursor.getColumnIndexOrThrow(COL_CODE)));
            String desc = cursor.getString(
                    cursor.getColumnIndexOrThrow(COL_DESC));
            if(desc.length() > UNCOMP_DESC_CUTOFF_LENGTH){
                backlogTaskBinding.taskDesc.setText(desc.substring(0, UNCOMP_DESC_CUTOFF_LENGTH));
            }
            else {
                backlogTaskBinding.taskDesc.setText(desc);
            }
            backlogTaskBinding.taskPercDone.setText(
                    mContext.getString(R.string.focus_perc_done_format,
                            cursor.getFloat(cursor.getColumnIndexOrThrow(COL_PERC))));
        }
    }
}
