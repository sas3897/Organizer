package com.aechkae.organizer.focus.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aechkae.organizer.focus.schemas.FocusDBAdapter;
import com.aechkae.organizer.focus.schemas.Task;
import com.aechkae.organizer.R;
import com.aechkae.organizer.focus.view_holders.BacklogTaskViewHolder;

public class BacklogTaskRVAdapter extends RecyclerView.Adapter<BacklogTaskViewHolder>{
    private Context mContext;
    private Cursor mCursor;
    private FocusDBAdapter db_adapter;

    public BacklogTaskRVAdapter(Context context, Cursor cursor, FocusDBAdapter dbAdapter){
        mContext = context;
        mCursor = cursor;
        db_adapter = dbAdapter;
    }

    @Override
    public int getItemCount(){
        return mCursor.getCount();
    }

    @Override
    public void onBindViewHolder(BacklogTaskViewHolder holder, int position){
        mCursor.moveToPosition(position);
        holder.bindCursor(mCursor, mContext);

        //Remove task from DB
        holder.getBacklogTaskBinding().backlogDeleteBtn.setOnClickListener((view) -> {
            db_adapter.deleteTask(
                    new String[]{holder.getBacklogTaskBinding().taskCode.getText().toString()});
        });

        //Move to active
        holder.getBacklogTaskBinding().backlogMvActiveBtn.setOnClickListener((view) ->
            db_adapter.moveTask(
                    new String[]{holder.getBacklogTaskBinding().
                            taskCode.getText().toString()}, Task.TaskType.ACTIVE)
        );
    }

    @Override
    public BacklogTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.backlog_task_item, parent, false);
        return new BacklogTaskViewHolder(view);
    }


}
