package com.aechkae.organizer.focus.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aechkae.organizer.database.OrgDBAdapter;
import com.aechkae.organizer.databinding.ActiveTaskItemBinding;
import com.aechkae.organizer.focus.UncompTask;
import com.aechkae.organizer.focus.Task.TaskType;
import com.aechkae.organizer.R;
import com.aechkae.organizer.focus.view_holders.ActiveTaskViewHolder;

import java.util.List;

import static com.aechkae.organizer.focus.Task.UNCOMP_DESC_CUTOFF_LENGTH;

public class ActiveTaskRVAdapter extends RecyclerView.Adapter<ActiveTaskViewHolder>{
    private OrgDBAdapter db_adapter;
    private Context mContext;
    private List<UncompTask> activeTasks;

    String TAG = "t";

    public ActiveTaskRVAdapter(List<UncompTask> tasks, OrgDBAdapter db_adapter, Context context){
        activeTasks = tasks;
        this.db_adapter = db_adapter;
        mContext = context;
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
        vhBinding.activeTaskDeleteBtn.setOnClickListener((view) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage(R.string.focus_delete_task_prompt);
            builder.setCancelable(false);
            builder.setNegativeButton(R.string.cancel_action, (dialog, id) -> {});
            builder.setPositiveButton(R.string.confirm_deletion, (dialog, id) -> {
                //TODO update
                db_adapter.deleteTask(
                        new String[]{vhBinding.activeTaskCode.getText().toString()});
                activeTasks.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            });

            AlertDialog alert = builder.create();
            alert.show();
        });


        //Move to backlog
        vhBinding.activeMvBacklogBtn.setOnClickListener((view) -> {
            Log.d(TAG, "pre-move");
            db_adapter.moveTask(
                    new String[]{vhBinding.activeTaskCode.getText().toString()}, TaskType.BACKLOG);
            activeTasks.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
        });

        //Complete task
        vhBinding.completeTaskBtn.setOnClickListener((view) -> {
            db_adapter.completeTask(
                    vhBinding.activeTaskCode.getText().toString(),
                    vhBinding.activeTaskDesc.getText().toString()
            );

            activeTasks.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
        });
    }

    @Override
    public ActiveTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.active_task_item, parent, false);
        return new ActiveTaskViewHolder(view);
    }


}
