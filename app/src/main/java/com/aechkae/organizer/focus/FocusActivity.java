package com.aechkae.organizer.focus;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.aechkae.organizer.focus.adapters.ActiveTaskRecyclerViewAdapter;
import com.aechkae.organizer.focus.adapters.BacklogTaskRecyclerViewAdapter;
import com.aechkae.organizer.focus.adapters.CompletedTaskRecyclerViewAdapter;
import com.aechkae.organizer.focus.schemas.CompletedTaskTable;
import com.aechkae.organizer.focus.schemas.FocusDBHelper;
import com.aechkae.organizer.focus.schemas.TaskType;
import com.aechkae.organizer.focus.schemas.UncompletedTasksTable;
import com.aechkae.organizer.R;
import com.aechkae.organizer.databinding.ActivityFocusBinding;
import com.aechkae.organizer.notable.NotableActivity;
import com.aechkae.organizer.reminder.ReminderActivity;
import com.aechkae.organizer.timer.TimerActivity;

//ArrayList<Task> backlogTasks = new ArrayList<>(Arrays.asList(
//        new Task("HIGHWY", "Get used to driving on the highway", 10),
//        new Task("BUDGET", "Budget out stuff, formalize procedure, etc", 40),
//        new Task("EXRCIS", "Create a routine for exercising, including diet", 5),
//        new Task("BONEST", "Write first draft of 'Bone Stronger than Bone'", 0),
//        new Task("DRKNGT", "Write first draft of 'Dark was the Night'", 50)
//        ));

public class FocusActivity extends AppCompatActivity {

    private ActivityFocusBinding activityFocusBinding;

    private boolean display_search = false;

    //TODO Need to show which 'sprint' we're in, and maybe a prompt to choose for that

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFocusBinding= DataBindingUtil.setContentView(this, R.layout.activity_focus);
        //Toolbar and Navbar
        setSupportActionBar(findViewById(R.id.focus_toolbar));
        getSupportActionBar().setTitle(R.string.module2_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        activityFocusBinding.focusNavBar.setNavigationItemSelectedListener((menuItem) -> {
            menuItem.setChecked(true);
            activityFocusBinding.focusDrawer.closeDrawers();

            //Swap the present activity
            switch(menuItem.getItemId()){
                case R.id.nav_focus: //We're in Focus already
                    break;
                case R.id.nav_reminder:
                    startActivity(new Intent(this, ReminderActivity.class));
                    break;
                case R.id.nav_timer:
                    startActivity(new Intent(this, TimerActivity.class));
                    break;
                case R.id.nav_notable:
                    startActivity(new Intent(this, NotableActivity.class));
                    break;
                default:
                    Toast.makeText(this, "Unknown nav item pressed", Toast.LENGTH_LONG)
                            .show();
            }

            return true;
        });

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
                display_search = true;
                invalidateOptionsMenu();
                showCompletedTasks();
                return true;
            case R.id.focus_how_to_use:
                display_search = false;
                invalidateOptionsMenu();
                goToHowToUse();
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
            //Module navigation menu button
            case android.R.id.home:
                activityFocusBinding.focusDrawer.openDrawer(GravityCompat.START);
                return true;
            default:
                Toast.makeText(this, "Some item was pressed that this doesn't handle.", Toast.LENGTH_LONG)
                        .show();
                return true;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //Hide the search icon if necessary
        menu.findItem(R.id.focus_search).setVisible(display_search);
        return super.onPrepareOptionsMenu(menu);
    }

    private void showBacklog(){
        // Demarcate which tab is active
        activityFocusBinding.addNewTaskBtn.setVisibility(View.VISIBLE);

        SQLiteDatabase taskDatabase = new FocusDBHelper(this).getReadableDatabase();

        String[] projection = {
                UncompletedTasksTable.COL_CODE,
                UncompletedTasksTable.COL_DESC,
                UncompletedTasksTable.COL_PERC
        };

        String selectionClause = UncompletedTasksTable.COL_TYPE + " = ?";
        String [] selectionArgs = {TaskType.BACKLOG + ""};

        Cursor db_cursor = taskDatabase.query(
                UncompletedTasksTable.TABLE_NAME,
                projection,
                selectionClause,
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
    private void showActiveTasks(){
        activityFocusBinding.addNewTaskBtn.setVisibility(View.GONE);
        SQLiteDatabase taskDatabase = new FocusDBHelper(this).getReadableDatabase();

        String[] projection = {
                UncompletedTasksTable.COL_CODE,
                UncompletedTasksTable.COL_DESC,
                UncompletedTasksTable.COL_PERC
        };

        String selectionClause = UncompletedTasksTable.COL_TYPE + " = ?";
        String [] selectionArgs = {TaskType.ACTIVE + ""};

        Cursor db_cursor = taskDatabase.query(
                UncompletedTasksTable.TABLE_NAME,
                projection,
                selectionClause,
                selectionArgs,
                null,
                null,
                null);

        activityFocusBinding.displayedTaskList.setAdapter(new ActiveTaskRecyclerViewAdapter(this, db_cursor));

        //TODO Display the back-burner tasks
    }

    private void showCompletedTasks(){
        activityFocusBinding.addNewTaskBtn.setVisibility(View.GONE);

        SQLiteDatabase taskDatabase = new FocusDBHelper(this).getReadableDatabase();

        String[] projection = {
                CompletedTaskTable.COL_CODE,
                CompletedTaskTable.COL_DESC,
                CompletedTaskTable.COL_COMP_DATE
        };
        String orderbyClause = CompletedTaskTable.COL_COMP_DATE + " DESC";
        
        Cursor db_cursor = taskDatabase.query(
                CompletedTaskTable.TABLE_NAME, 
                projection, 
                null, 
                null, 
                null, 
                null, 
                orderbyClause);

        activityFocusBinding.displayedTaskList.setAdapter(new CompletedTaskRecyclerViewAdapter(db_cursor));
    }

    private void goToHowToUse(){
        startActivity(new Intent(this, HowToUseActivity.class));
    }

    public void goToTaskCreationPage(View v){
        startActivity(new Intent(this, AddTaskActivity.class));
    }
}
