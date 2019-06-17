package com.aechkae.organizer.focus.view_holders;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aechkae.organizer.databinding.CompletedTaskItemBinding;

public class CompTaskViewHolder extends RecyclerView.ViewHolder{
    private CompletedTaskItemBinding compTaskItemBinding;
    public CompTaskViewHolder(View itemView){
        super(itemView);
        compTaskItemBinding = DataBindingUtil.bind(itemView);
    }

    public CompletedTaskItemBinding getCompTaskItemBinding() {
        return compTaskItemBinding;
    }

}
