package com.aechkae.organizer.focus;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aechkae.organizer.databinding.ActivityAddTaskBinding;
import com.aechkae.organizer.focus.schemas.FocusDBHelper;
import com.aechkae.organizer.focus.schemas.TaskType;
import com.aechkae.organizer.R;

import static com.aechkae.organizer.focus.schemas.UncompletedTasksTable.COL_CODE;
import static com.aechkae.organizer.focus.schemas.UncompletedTasksTable.COL_DESC;
import static com.aechkae.organizer.focus.schemas.UncompletedTasksTable.COL_PERC;
import static com.aechkae.organizer.focus.schemas.UncompletedTasksTable.COL_TYPE;
import static com.aechkae.organizer.focus.schemas.UncompletedTasksTable.TABLE_NAME;

public class AddTaskActivity extends AppCompatActivity {

    private ActivityAddTaskBinding actBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_task);
    }

    public void saveToDB(View v){
        SQLiteDatabase task_database = new FocusDBHelper(this).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_CODE, actBinding.idCodeEdit.getText().toString());
        values.put(COL_DESC, actBinding.descEdit.getText().toString());
        values.put(COL_PERC, Float.parseFloat(actBinding.percDoneEdit.getText().toString()));
        values.put(COL_TYPE, TaskType.BACKLOG);

        long newRowID = task_database.insert(TABLE_NAME, null, values);

        if(newRowID == -1) {
            Toast.makeText(this, "Task not added. ID likely duplicate.", Toast.LENGTH_LONG)
                    .show();
        } else{
            Toast.makeText(this, "New task with ID: " + newRowID + " added to backlog", Toast.LENGTH_LONG)
                    .show();
        }

        finish();
    }
}
