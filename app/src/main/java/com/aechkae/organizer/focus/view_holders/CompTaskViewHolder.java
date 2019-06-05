package com.aechkae.organizer.focus.view_holders;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aechkae.organizer.databinding.CompletedTaskItemBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.aechkae.organizer.focus.schemas.CompTaskTable.COL_CODE;
import static com.aechkae.organizer.focus.schemas.CompTaskTable.COL_COMP_DATE;
import static com.aechkae.organizer.focus.schemas.CompTaskTable.COL_DESC;
import static com.aechkae.organizer.focus.schemas.Task.COMP_DESC_CUTOFF_LENGTH;

public class CompTaskViewHolder extends RecyclerView.ViewHolder{
    private CompletedTaskItemBinding completedTaskItemBinding;
    public CompTaskViewHolder(View itemView){
        super(itemView);
        completedTaskItemBinding = DataBindingUtil.bind(itemView);
    }

    public void bindCursor(Cursor cursor){
        completedTaskItemBinding.compTaskCode.setText(cursor.getString(
                cursor.getColumnIndexOrThrow(COL_CODE)));

        String desc = cursor.getString(
                cursor.getColumnIndexOrThrow(COL_DESC));
        if(desc.length() > COMP_DESC_CUTOFF_LENGTH){
            completedTaskItemBinding.compTaskDesc.setText(desc.substring(0, COMP_DESC_CUTOFF_LENGTH));
        }
        else {
            completedTaskItemBinding.compTaskDesc.setText(desc);
        }

        Calendar cal  = Calendar.getInstance();
        cal.setTimeInMillis(cursor.getLong(cursor.getColumnIndexOrThrow(COL_COMP_DATE)));
        completedTaskItemBinding.dateTaskComp.setText(new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(cal.getTime()));
    }
}
