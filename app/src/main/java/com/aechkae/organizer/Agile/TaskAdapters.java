package com.aechkae.organizer.Agile;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.aechkae.organizer.Agile.common.Task;
import com.aechkae.organizer.R;

import java.util.ArrayList;

/**
 * ArrayAdapters for elements.
 * Structure taken from http://ezzylearning.com/tutorial/customizing-android-listview-items-with-custom-arrayadapter
 */


class TaskAdapter extends ArrayAdapter<Task>{
    Context context;
    int layoutResourceId;
    ArrayList<Task> tasks;

    TaskAdapter(Context context, int layoutResourceId, ArrayList<Task> tasks){
        super(context, layoutResourceId, tasks);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.tasks = tasks;
    }
}

class BacklogTaskAdapter extends TaskAdapter{

    BacklogTaskAdapter(Context context, int layoutResourceId, ArrayList<Task> tasks){
        super(context, layoutResourceId, tasks);
    }

    private class BacklogTaskHolder {
        TextView id;
        TextView desc;
        TextView perc;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        BacklogTaskHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new BacklogTaskHolder();
            holder.id =  row.findViewById(R.id.task_code);
            holder.desc = row.findViewById(R.id.task_desc);
            holder.perc = row.findViewById(R.id.task_perc_done);

            row.setTag(holder);
        }
        else
        {
            holder = (BacklogTaskHolder) row.getTag();
        }

        Task Task = tasks.get(position);
        holder.id.setText(Task.idCode);
        holder.desc.setText(Task.description);
        holder.perc.setText(Double.toString(Task.percentageDone));

        return row;
    }
}

class ActiveTaskAdapter extends TaskAdapter{

    ActiveTaskAdapter(Context context, int layoutResourceId, ArrayList<Task> tasks){
        super(context, layoutResourceId, tasks);
    }

    private class ActiveTaskHolder {
        TextView id;
        TextView desc;
        TextView perc;
        Button deleteBtn;
        Button moveToBackburnerBtn;
        Button moveToBacklogBtn;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ActiveTaskHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ActiveTaskHolder();
            holder.id =  row.findViewById(R.id.task_code);
            holder.desc = row.findViewById(R.id.task_desc);
            holder.perc = row.findViewById(R.id.task_perc_done);
            holder.deleteBtn = row.findViewById(R.id.task_delete_btn);

            row.setTag(holder);
        }
        else
        {
            holder = (ActiveTaskHolder) row.getTag();
        }

        Task Task = tasks.get(position);
        holder.id.setText(Task.idCode);
        holder.desc.setText(Task.description);
        holder.perc.setText(Double.toString(Task.percentageDone));

        //TODO
        holder.deleteBtn.setOnClickListener((View v) -> {
            tasks.remove(position);
            notifyDataSetChanged();
        });

        return row;
    }
}


