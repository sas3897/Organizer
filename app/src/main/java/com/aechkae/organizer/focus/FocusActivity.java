package com.aechkae.organizer.focus;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.aechkae.organizer.focus.adapters.ActiveTaskRVAdapter;
import com.aechkae.organizer.focus.adapters.BacklogTaskRVAdapter;
import com.aechkae.organizer.focus.adapters.CompTaskRVAdapter;
import com.aechkae.organizer.focus.schemas.Task.TaskType;
import com.aechkae.organizer.R;
import com.aechkae.organizer.databinding.ActivityFocusBinding;
import com.aechkae.organizer.notable.NotableActivity;
import com.aechkae.organizer.reminder.ReminderActivity;
import com.aechkae.organizer.timer.TimerActivity;
import com.aechkae.organizer.focus.schemas.FocusDBAdapter;

public class FocusActivity extends AppCompatActivity {

    private ActivityFocusBinding activityFocusBinding;
    private FocusDBAdapter db_adapter;

    private boolean display_search = false;

    //TODO Need to show which 'sprint' we're in, and maybe a prompt to choose for that

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db_adapter = new FocusDBAdapter(this);  //TODO is this the proper context?
        db_adapter.openDB();
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
                    db_adapter.closeDB();
                    break;
                case R.id.nav_timer:
                    startActivity(new Intent(this, TimerActivity.class));
                    db_adapter.closeDB();
                    break;
                case R.id.nav_notable:
                    startActivity(new Intent(this, NotableActivity.class));
                    db_adapter.closeDB();
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

        Cursor db_cursor = db_adapter.getAllUncompTaskOfType(TaskType.BACKLOG);
        activityFocusBinding.displayedTaskList.setAdapter(new BacklogTaskRVAdapter(this, db_cursor));

    }

    /**
     * Displays the (up to) three active and three backburner tasks found in the "uncompleted_tasks"
     * table,
     */
    private void showActiveTasks(){
        activityFocusBinding.addNewTaskBtn.setVisibility(View.GONE);
        Cursor db_cursor = db_adapter.getAllUncompTaskOfType(TaskType.ACTIVE);

        activityFocusBinding.displayedTaskList.setAdapter(new ActiveTaskRVAdapter(this, db_cursor));

        //TODO Display the back-burner tasks
    }

    private void showCompletedTasks(){
        activityFocusBinding.addNewTaskBtn.setVisibility(View.GONE);

        Cursor db_cursor = db_adapter.getAllCompTask();

        activityFocusBinding.displayedTaskList.setAdapter(new CompTaskRVAdapter(db_cursor));
    }

    private void goToHowToUse(){
        startActivity(new Intent(this, HowToUseActivity.class));
    }

    public void goToTaskCreationPage(View v){
        startActivity(new Intent(this, AddTaskActivity.class));
    }
}
