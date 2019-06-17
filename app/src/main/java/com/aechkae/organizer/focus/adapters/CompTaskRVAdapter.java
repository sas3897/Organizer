package com.aechkae.organizer.focus.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aechkae.organizer.R;
import com.aechkae.organizer.databinding.CompletedTaskItemBinding;
import com.aechkae.organizer.focus.CompTask;
import com.aechkae.organizer.focus.view_holders.CompTaskViewHolder;

import java.util.List;

import static com.aechkae.organizer.focus.Task.COMP_DESC_CUTOFF_LENGTH;

public class CompTaskRVAdapter extends RecyclerView.Adapter<CompTaskViewHolder> {
    private List<CompTask> compTasks;

    public CompTaskRVAdapter(List<CompTask> tasks){
        compTasks = tasks;
    }

    public void setCompTasks(List<CompTask> compTasks) {
        this.compTasks = compTasks;
    }

    @Override
    public int getItemCount(){
        return compTasks.size();
    }

    @Override
    public void onBindViewHolder(CompTaskViewHolder holder, int position) {
        CompletedTaskItemBinding vhBinding = holder.getCompTaskItemBinding();
        CompTask vhTask = compTasks.get(position);

        vhBinding.compTaskCode.setText(vhTask.idCode);

        // TODO this is bad and should occur automatically
        if(vhTask.description.length() > COMP_DESC_CUTOFF_LENGTH){
            vhBinding.compTaskDesc.setText(vhTask.description.substring(0, COMP_DESC_CUTOFF_LENGTH));
        }
        else{
            vhBinding.compTaskDesc.setText(vhTask.description);
        }

        vhBinding.dateTaskComp.setText(vhTask.compDate);
    }

    @Override
    public CompTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.completed_task_item, parent, false);
        return new CompTaskViewHolder(view);
    }


}
