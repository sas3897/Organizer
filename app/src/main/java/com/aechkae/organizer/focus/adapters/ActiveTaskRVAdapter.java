package com.aechkae.organizer.focus.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aechkae.organizer.database.OrgDBAdapter;
import com.aechkae.organizer.databinding.ActiveTaskItemBinding;
import com.aechkae.organizer.focus.UncompTask;
import com.aechkae.organizer.focus.Task;
import com.aechkae.organizer.R;
import com.aechkae.organizer.focus.view_holders.ActiveTaskViewHolder;

import java.util.List;

import static com.aechkae.organizer.focus.Task.UNCOMP_DESC_CUTOFF_LENGTH;

public class ActiveTaskRVAdapter extends RecyclerView.Adapter<ActiveTaskViewHolder>{
    private OrgDBAdapter db_adapter;

    private List<UncompTask> activeTasks;

    public ActiveTaskRVAdapter(List<UncompTask> tasks, OrgDBAdapter db_adapter){
        activeTasks = tasks;
        this.db_adapter = db_adapter;
    }

    public void setActiveTasks(List<UncompTask> activeTasks) {
        this.activeTasks = activeTasks;
    }

    @Override
    public int getItemCount(){
        return activeTasks.size();
    }

    @Override
    public void onBindViewHolder(ActiveTaskViewHolder holder, int position) {
        ActiveTaskItemBinding vhBinding = holder.getActiveTaskItemBinding();
        UncompTask vhTask = activeTasks.get(position);

        vhBinding.activeTaskCode.setText(vhTask.idCode);
        if(vhTask.description.length() > UNCOMP_DESC_CUTOFF_LENGTH){
            vhBinding.activeTaskDesc.setText(vhTask.description.substring(0, UNCOMP_DESC_CUTOFF_LENGTH));
        }
        else{
            vhBinding.activeTaskDesc.setText(vhTask.description);
        }
        vhBinding.activeTaskPercDone.setText(vhTask.perc_done);


        //Remove task from DB
        holder.getActiveTaskItemBinding().activeTaskDeleteBtn.setOnClickListener((view) -> {
            db_adapter.deleteTask(
                    new String[]{vhBinding.activeTaskCode.getText().toString()});
            activeTasks.remove(position);
            notifyItemChanged(position);
        });


        //Move to backlog
        holder.getActiveTaskItemBinding().activeMvBacklogBtn.setOnClickListener((view) -> {
            db_adapter.moveTask(
                    new String[]{vhBinding.activeTaskCode.getText().toString()}, Task.TaskType.BACKLOG);
            activeTasks.remove(position);
            notifyItemChanged(position);
        });

        //Complete task
        holder.getActiveTaskItemBinding().completeTaskBtn.setOnClickListener((view) -> {
            db_adapter.completeTask(
                    vhBinding.activeTaskCode.getText().toString(),
                    vhBinding.activeTaskDesc.getText().toString()
            );

            activeTasks.remove(position);
            notifyItemChanged(position);
        });
    }

    @Override
    public ActiveTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.active_task_item, parent, false);
        return new ActiveTaskViewHolder(view);
    }


}
