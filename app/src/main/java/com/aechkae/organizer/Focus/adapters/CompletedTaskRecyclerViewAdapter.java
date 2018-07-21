package com.aechkae.organizer.Focus.adapters;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aechkae.organizer.R;
import com.aechkae.organizer.databinding.CompletedTaskItemBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.aechkae.organizer.Focus.schemas.CompletedTaskTable.COL_COMP_DATE;
import static com.aechkae.organizer.Focus.schemas.Task.COMP_DESC_CUTOFF_LENGTH;
import static com.aechkae.organizer.Focus.schemas.CompletedTaskTable.COL_CODE;
import static com.aechkae.organizer.Focus.schemas.CompletedTaskTable.COL_DESC;

public class CompletedTaskRecyclerViewAdapter extends RecyclerView.Adapter<CompletedTaskRecyclerViewAdapter.CompletedTaskViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public CompletedTaskRecyclerViewAdapter(Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }

    @Override
    public int getItemCount(){
        return mCursor.getCount();
    }

    @Override
    public void onBindViewHolder(CompletedTaskViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.bindCursor(mCursor);
    }

    @Override
    public CompletedTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.completed_task_item, parent, false);
        return new CompletedTaskViewHolder(view);
    }

    class CompletedTaskViewHolder extends RecyclerView.ViewHolder{
        CompletedTaskItemBinding completedTaskItemBinding;
        CompletedTaskViewHolder(View itemView){
            super(itemView);
            completedTaskItemBinding = DataBindingUtil.bind(itemView);
        }

        void bindCursor(Cursor cursor){
            //TODO
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
}
