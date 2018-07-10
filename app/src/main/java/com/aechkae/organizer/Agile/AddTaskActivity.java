package com.aechkae.organizer.Agile;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aechkae.organizer.Agile.common.TaskType;
import com.aechkae.organizer.R;

import static com.aechkae.organizer.Agile.common.UncompletedTasksTable.COL_CODE;
import static com.aechkae.organizer.Agile.common.UncompletedTasksTable.COL_DESC;
import static com.aechkae.organizer.Agile.common.UncompletedTasksTable.COL_PERC;
import static com.aechkae.organizer.Agile.common.UncompletedTasksTable.COL_TYPE;
import static com.aechkae.organizer.Agile.common.UncompletedTasksTable.TABLE_NAME;

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

        SQLiteDatabase task_database = new AgileDBHelper(this).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_CODE, id_code_edit.getText().toString());
        values.put(COL_DESC, desc_edit.getText().toString());
        values.put(COL_PERC, Float.parseFloat(perc_done_edit.getText().toString()));
        values.put(COL_TYPE, TaskType.BACKLOG.getType());

        long newRowID = task_database.insert(TABLE_NAME, null, values);

        Toast.makeText(this, "The new task's ID is: " + newRowID, Toast.LENGTH_LONG).show();

        startActivity(new Intent(this, AgileActivity.class));
    }
}
