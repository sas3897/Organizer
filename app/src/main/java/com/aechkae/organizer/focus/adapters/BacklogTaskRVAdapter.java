package com.aechkae.organizer.focus.adapters;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aechkae.organizer.database.OrgDBAdapter;
import com.aechkae.organizer.databinding.BacklogTaskItemBinding;
import com.aechkae.organizer.focus.Task.TaskType;
import com.aechkae.organizer.R;
import com.aechkae.organizer.focus.UncompTask;
import com.aechkae.organizer.focus.view_holders.BacklogTaskViewHolder;

import java.util.List;


public class BacklogTaskRVAdapter extends RecyclerView.Adapter<BacklogTaskViewHolder>{
    private OrgDBAdapter db_adapter;
    private Context mContext;
    private List<UncompTask> backlogTasks;

    public BacklogTaskRVAdapter(List<UncompTask> tasks, OrgDBAdapter dbAdapter, Context context){
        db_adapter = dbAdapter;
        backlogTasks = tasks;
        mContext = context;
    }

    @Override
    public int getItemCount(){
        return backlogTasks.size();
    }

    public void setBacklogTasks(List<UncompTask> backlogTasks) {
        this.backlogTasks = backlogTasks;
    }

    @Override
    public void onBindViewHolder(BacklogTaskViewHolder holder, int position){
        BacklogTaskItemBinding vhBinding = holder.getBacklogTaskBinding();
        UncompTask vhTask = backlogTasks.get(position);

        vhBinding.taskCode.setText(vhTask.idCode);
        vhBinding.taskDesc.setText(vhTask.description);
        vhBinding.taskPercDone.setText(vhTask.perc_done);

        //Remove task from DB
        vhBinding.backlogDeleteBtn.setOnClickListener((view) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage(R.string.focus_delete_task_prompt);
            builder.setCancelable(false);
            builder.setNegativeButton(R.string.cancel_action, (dialog, id) -> {});
            builder.setPositiveButton(R.string.confirm_deletion, (dialog, id) -> {
                db_adapter.deleteTask(
                        new String[]{vhBinding.taskCode.getText().toString()});
                backlogTasks.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            });

            AlertDialog alert = builder.create();
            alert.show();
        });

        //Move to active
        vhBinding.backlogMvActiveBtn.setOnClickListener((view) -> {
            db_adapter.moveTask(
                    new String[]{vhBinding.taskCode.getText().toString()}, TaskType.ACTIVE);
            backlogTasks.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
        });
    }

    @Override
    public BacklogTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.backlog_task_item, parent, false);
        return new BacklogTaskViewHolder(view);
    }


}
