package com.aechkae.organizer.focus.view_holders;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aechkae.organizer.databinding.ActiveTaskItemBinding;

public class ActiveTaskViewHolder extends RecyclerView.ViewHolder{
    private ActiveTaskItemBinding activeTaskItemBinding;

    public ActiveTaskViewHolder(View itemView){
        super(itemView);
        activeTaskItemBinding = DataBindingUtil.bind(itemView);
    }

    public ActiveTaskItemBinding getActiveTaskItemBinding() {
        return activeTaskItemBinding;
    }
}
