package com.aechkae.organizer.focus.view_holders;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aechkae.organizer.R;
import com.aechkae.organizer.databinding.ActiveTaskItemBinding;

import static com.aechkae.organizer.focus.schemas.Task.UNCOMP_DESC_CUTOFF_LENGTH;
import static com.aechkae.organizer.focus.schemas.UncompTaskTable.COL_CODE;
import static com.aechkae.organizer.focus.schemas.UncompTaskTable.COL_DESC;
import static com.aechkae.organizer.focus.schemas.UncompTaskTable.COL_PERC;

public class ActiveTaskViewHolder extends RecyclerView.ViewHolder{
    private ActiveTaskItemBinding activeTaskItemBinding;

    public ActiveTaskViewHolder(View itemView){
        super(itemView);
        activeTaskItemBinding = DataBindingUtil.bind(itemView);
    }

    public ActiveTaskItemBinding getActiveTaskItemBinding() {
        return activeTaskItemBinding;
    }

    public void bindCursor(Cursor cursor, Context mContext){
        activeTaskItemBinding.activeTaskCode.setText(cursor.getString(
                cursor.getColumnIndexOrThrow(COL_CODE)));

        String desc = cursor.getString(
                cursor.getColumnIndexOrThrow(COL_DESC));
        if(desc.length() > UNCOMP_DESC_CUTOFF_LENGTH){
            activeTaskItemBinding.activeTaskDesc.setText(desc.substring(0, UNCOMP_DESC_CUTOFF_LENGTH));
        }
        else {
            activeTaskItemBinding.activeTaskDesc.setText(desc);
        }
        activeTaskItemBinding.activeTaskPercDone.setText(
                mContext.getString(R.string.focus_perc_done_format,
                        cursor.getFloat(cursor.getColumnIndexOrThrow(COL_PERC))));
    }
}
