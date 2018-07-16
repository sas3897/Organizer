package com.aechkae.organizer.Focus;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.aechkae.organizer.Focus.schemas.Task;
import com.aechkae.organizer.Focus.schemas.TaskType;
import com.aechkae.organizer.Focus.schemas.UncompletedTasksTable;
import com.aechkae.organizer.R;
import com.aechkae.organizer.databinding.ActivityFocusBinding;

import java.util.ArrayList;
import java.util.Arrays;

//ArrayList<Task> backlogTasks = new ArrayList<>(Arrays.asList(
//        new Task("FINSOA", "Finish writing the first draft of 'A Scarf of Arms'", 80),
//        new Task("BACKUP", "Make a backup of the data on Hephaestus and Demeter", 0),
//        new Task("ORGNZR", "Complete Organizer's calendar, agile, and alarm modules", 1),
//        new Task("HIGHWY", "Get used to driving on the highway", 10),
//        new Task("EDIBLE", "Bake some cookies/brownies", 20),
//        new Task("BUDGET", "Budget out stuff, formalize procedure, etc", 40),
//        new Task("EXRCIS", "Create a routine for exercising, including diet", 5),
//        new Task("VGROTH", "Set up the Roth IRA with Vanguard and make the 2017/2018 contributions", 0),
//        new Task("BONEST", "Write first draft of 'Bone Stronger than Bone'", 0),
//        new Task("DRKNGT", "Write first draft of 'Dark was the Night'", 50)
//        ));

public class FocusActivity extends AppCompatActivity {

    private ActivityFocusBinding activityFocusBinding;

    private RecyclerView.Adapter mAdapter;

    private boolean display_search = false;

    //TODO Need to show which 'sprint' we're in, and maybe a prompt to choose for that

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFocusBinding= DataBindingUtil.setContentView(this, R.layout.activity_focus);
        setSupportActionBar(activityFocusBinding.focusToolbar);
        getSupportActionBar().setTitle(R.string.module2_name);

        //RecyclerView attributes
        activityFocusBinding.displayedTaskList.setLayoutManager(new LinearLayoutManager(this));


        showActiveTasks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.focus_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.active_tasks_item:
                display_search = false;
                invalidateOptionsMenu();
                showActiveTasks();
                return true;
            case R.id.backlog_tasks_item:
                display_search = true;
                invalidateOptionsMenu();
                showBacklog();
                return true;
            case R.id.completed_tasks_item:
                Toast.makeText(this, "Completed Tasks item selected", Toast.LENGTH_LONG)
                        .show();
                display_search = true;
                invalidateOptionsMenu();
                return true;
            case R.id.focus_about_item:
                Toast.makeText(this, "About Focus item selected", Toast.LENGTH_LONG)
                        .show();
                display_search = false;
                invalidateOptionsMenu();
                return true;
            case R.id.focus_settings_item:
                Toast.makeText(this, "Focus Settings item selected", Toast.LENGTH_LONG)
                        .show();
                display_search = false;
                invalidateOptionsMenu();
                return true;
            case R.id.focus_search:
                Toast.makeText(this, "Focus Search item selected", Toast.LENGTH_LONG)
                        .show();
                return true;
            default:
                Toast.makeText(this, "Some item was pressed that this doesn't handle.", Toast.LENGTH_LONG)
                        .show();
                return true;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(Build.VERSION.SDK_INT > 11) {
            menu.findItem(R.id.focus_search).setVisible(display_search);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void showBacklog(){
        // Demarcate which tab is active
        activityFocusBinding.addNewTaskBtn.setVisibility(View.VISIBLE);

        SQLiteDatabase taskDatabase = new FocusDBHelper(this).getReadableDatabase();

        String[] projection = {
                UncompletedTasksTable.COL_CODE,
                UncompletedTasksTable.COL_DESC,
                UncompletedTasksTable.COL_PERC
        };

        String selection = UncompletedTasksTable.COL_TYPE + " = ?";
        String [] selectionArgs = {TaskType.BACKLOG + ""};

        Cursor db_cursor = taskDatabase.query(
                UncompletedTasksTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        activityFocusBinding.displayedTaskList.setAdapter(new BacklogTaskRecyclerViewAdapter(this, db_cursor));

    }

    /**
     * Displays the (up to) three active and three backburner tasks found in the "uncompleted_tasks"
     * table,
     */
    public void showActiveTasks(){
        activityFocusBinding.addNewTaskBtn.setVisibility(View.GONE);
        SQLiteDatabase taskDatabase = new FocusDBHelper(this).getReadableDatabase();

        String[] projection = {
                UncompletedTasksTable.COL_CODE,
                UncompletedTasksTable.COL_DESC,
                UncompletedTasksTable.COL_PERC
        };

        String selection = UncompletedTasksTable.COL_TYPE + " = ?";
        String [] selectionArgs = {TaskType.ACTIVE + ""};

        Cursor db_cursor = taskDatabase.query(
                UncompletedTasksTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        activityFocusBinding.displayedTaskList.setAdapter(new ActiveTaskRecyclerViewAdapter(this, db_cursor));

        //TODO Display the back-burner tasks
    }

    public void goToTaskCreationPage(View v){
        startActivity(new Intent(this, AddTaskActivity.class));
    }

    public void deleteTask(View v){

    }

    public void completeTask(Task at){
        //TODO Move to 'log'
    }
}
