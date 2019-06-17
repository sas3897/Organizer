package com.aechkae.organizer.focus.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aechkae.organizer.database.OrgDBAdapter;
import com.aechkae.organizer.databinding.ActiveTaskItemBinding;
import com.aechkae.organizer.databinding.BacklogTaskItemBinding;
import com.aechkae.organizer.focus.Task;
import com.aechkae.organizer.R;
import com.aechkae.organizer.focus.UncompTask;
import com.aechkae.organizer.focus.view_holders.BacklogTaskViewHolder;

import java.util.List;

import static com.aechkae.organizer.focus.Task.UNCOMP_DESC_CUTOFF_LENGTH;

public class BacklogTaskRVAdapter extends RecyclerView.Adapter<BacklogTaskViewHolder>{
    private OrgDBAdapter db_adapter;
    private List<UncompTask> backlogTasks;

    public BacklogTaskRVAdapter(List<UncompTask> tasks, OrgDBAdapter dbAdapter){
        db_adapter = dbAdapter;
        backlogTasks = tasks;
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
        if(vhTask.description.length() > UNCOMP_DESC_CUTOFF_LENGTH){
            vhBinding.taskDesc.setText(vhTask.description.substring(0, UNCOMP_DESC_CUTOFF_LENGTH));
        }
        else{
            vhBinding.taskDesc.setText(vhTask.description);
        }
        vhBinding.taskPercDone.setText(vhTask.perc_done);

        //Remove task from DB
        holder.getBacklogTaskBinding().backlogDeleteBtn.setOnClickListener((view) -> {
            db_adapter.deleteTask(
                    new String[]{holder.getBacklogTaskBinding().taskCode.getText().toString()});
            backlogTasks.remove(position);
            notifyItemChanged(position);
        });

        //Move to active
        holder.getBacklogTaskBinding().backlogMvActiveBtn.setOnClickListener((view) -> {
            db_adapter.moveTask(
                    new String[]{holder.getBacklogTaskBinding().
                            taskCode.getText().toString()}, Task.TaskType.ACTIVE);
            backlogTasks.remove(position);
            notifyItemChanged(position);
        });
    }

    @Override
    public BacklogTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.backlog_task_item, parent, false);
        return new BacklogTaskViewHolder(view);
    }


}
