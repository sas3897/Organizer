package com.aechkae.organizer.Focus;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aechkae.organizer.Focus.schemas.TaskType;
import com.aechkae.organizer.R;

import static com.aechkae.organizer.Focus.schemas.UncompletedTasksTable.COL_CODE;
import static com.aechkae.organizer.Focus.schemas.UncompletedTasksTable.COL_DESC;
import static com.aechkae.organizer.Focus.schemas.UncompletedTasksTable.COL_PERC;
import static com.aechkae.organizer.Focus.schemas.UncompletedTasksTable.COL_TYPE;
import static com.aechkae.organizer.Focus.schemas.UncompletedTasksTable.TABLE_NAME;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
    }

    public void saveToDB(View v){
        EditText id_code_edit = findViewById(R.id.id_code_edit);
        EditText desc_edit = findViewById(R.id.desc_edit);
        EditText perc_done_edit = findViewById(R.id.perc_done_edit);

        SQLiteDatabase task_database = new FocusDBHelper(this).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_CODE, id_code_edit.getText().toString());
        values.put(COL_DESC, desc_edit.getText().toString());
        values.put(COL_PERC, Float.parseFloat(perc_done_edit.getText().toString()));
        values.put(COL_TYPE, TaskType.BACKLOG);

        long newRowID = task_database.insert(TABLE_NAME, null, values);

        Toast.makeText(this, "New task with ID: " + newRowID + " added to backlog", Toast.LENGTH_LONG)
                .show();

        startActivity(new Intent(this, FocusActivity.class));
    }
}
