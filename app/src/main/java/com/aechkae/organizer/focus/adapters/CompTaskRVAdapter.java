package com.aechkae.organizer.focus.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aechkae.organizer.R;
import com.aechkae.organizer.focus.view_holders.CompTaskViewHolder;

public class CompTaskRVAdapter extends RecyclerView.Adapter<CompTaskViewHolder> {
    private Cursor mCursor;

    public CompTaskRVAdapter(Cursor cursor){
        mCursor = cursor;
    }

    @Override
    public int getItemCount(){
        return mCursor.getCount();
    }

    @Override
    public void onBindViewHolder(CompTaskViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.bindCursor(mCursor);
    }

    @Override
    public CompTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.completed_task_item, parent, false);
        return new CompTaskViewHolder(view);
    }


}
