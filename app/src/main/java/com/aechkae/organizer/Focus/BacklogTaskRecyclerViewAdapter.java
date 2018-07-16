package com.aechkae.organizer.Focus;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aechkae.organizer.Focus.common.UncompletedTasksTable;
import com.aechkae.organizer.R;
import com.aechkae.organizer.databinding.BacklogTaskItemBinding;

//TODO This is actually just for the backlog tasks. You'll need a different adapter for active ones
public class BacklogTaskRecyclerViewAdapter extends RecyclerView.Adapter<BacklogTaskRecyclerViewAdapter.TaskViewHolder>{
    Context mContext;
    Cursor mCursor;

    public BacklogTaskRecyclerViewAdapter(Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }

    @Override
    public int getItemCount(){
        return mCursor.getCount();
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position){
        mCursor.moveToPosition(position);
        holder.bindCursor(mCursor);

        holder.backlogTaskBinding.backlogDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO delete and update
            }
        });
        holder.backlogTaskBinding.backlogMvActiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO change this thing's table
            }
        });
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.backlog_task_item, parent, false);
        return new TaskViewHolder(view);
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder{
        BacklogTaskItemBinding backlogTaskBinding;

        public TaskViewHolder(View itemView){
            super(itemView);
            backlogTaskBinding = DataBindingUtil.bind(itemView);
        }

        public void bindCursor(Cursor cursor){
            backlogTaskBinding.taskCode.setText(cursor.getString(
                    cursor.getColumnIndexOrThrow(UncompletedTasksTable.COL_CODE)));
            backlogTaskBinding.taskDesc.setText(cursor.getString(
                    cursor.getColumnIndexOrThrow(UncompletedTasksTable.COL_DESC)));
            backlogTaskBinding.taskPercDone.setText("" + cursor.getDouble(
                    cursor.getColumnIndexOrThrow(UncompletedTasksTable.COL_PERC)));
        }
    }
}
