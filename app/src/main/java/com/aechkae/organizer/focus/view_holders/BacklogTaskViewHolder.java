package com.aechkae.organizer.focus.view_holders;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aechkae.organizer.databinding.BacklogTaskItemBinding;

public class BacklogTaskViewHolder extends RecyclerView.ViewHolder{
    private BacklogTaskItemBinding backlogTaskBinding;

    public BacklogTaskViewHolder(View itemView){
        super(itemView);
        backlogTaskBinding = DataBindingUtil.bind(itemView);
    }

    public BacklogTaskItemBinding getBacklogTaskBinding() {
        return backlogTaskBinding;
    }
}