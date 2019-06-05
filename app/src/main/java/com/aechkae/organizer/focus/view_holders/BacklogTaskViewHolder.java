package com.aechkae.organizer.focus.view_holders;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aechkae.organizer.R;
import com.aechkae.organizer.databinding.BacklogTaskItemBinding;

import static com.aechkae.organizer.focus.schemas.Task.UNCOMP_DESC_CUTOFF_LENGTH;
import static com.aechkae.organizer.focus.schemas.UncompTaskTable.COL_CODE;
import static com.aechkae.organizer.focus.schemas.UncompTaskTable.COL_DESC;
import static com.aechkae.organizer.focus.schemas.UncompTaskTable.COL_PERC;

public class BacklogTaskViewHolder extends RecyclerView.ViewHolder{
    private BacklogTaskItemBinding backlogTaskBinding;

    public BacklogTaskViewHolder(View itemView){
        super(itemView);
        backlogTaskBinding = DataBindingUtil.bind(itemView);
    }

    public BacklogTaskItemBinding getBacklogTaskBinding() {
        return backlogTaskBinding;
    }

    public void bindCursor(Cursor cursor, Context mContext){
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