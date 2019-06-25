package com.aechkae.organizer.focus;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.aechkae.organizer.focus.adapters.ActiveTaskRVAdapter;
import com.aechkae.organizer.focus.adapters.BacklogTaskRVAdapter;
import com.aechkae.organizer.focus.adapters.CompTaskRVAdapter;
import com.aechkae.organizer.focus.Task.TaskType;
import com.aechkae.organizer.R;
import com.aechkae.organizer.databinding.ActivityFocusBinding;
import com.aechkae.organizer.notable.NotableActivity;
import com.aechkae.organizer.reminder.ReminderActivity;
import com.aechkae.organizer.timer.TimerActivity;
import com.aechkae.organizer.database.OrgDBAdapter;

import java.util.List;

public class FocusActivity extends AppCompatActivity {

    private enum TaskPage{
        ACTIVE,
        BACKLOG,
        COMPLETED
    }

    private ActivityFocusBinding activityFocusBinding;
    private OrgDBAdapter db_adapter;
    private GestureDetectorCompat mDetector;

    private CompTaskRVAdapter comp_adapter = null;
    private ActiveTaskRVAdapter active_adapter = null;
    private BacklogTaskRVAdapter backlog_adapter = null;

    private boolean display_search = false;
    private TaskPage curr_page;

    //TODO Need to show which 'sprint' we're in, and maybe a prompt to choose for that

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db_adapter = new OrgDBAdapter(this);  //TODO is this the proper context?
        db_adapter.openDB();
        activityFocusBinding = DataBindingUtil.setContentView(this, R.layout.activity_focus);
        mDetector = new GestureDetectorCompat(this, new pageGestureListener());
        curr_page = TaskPage.ACTIVE;
        activityFocusBinding.displayedTaskList.setOnTouchListener( (view, motion) -> mDetector.onTouchEvent(motion));

        //Toolbar and Navbar
        setSupportActionBar(findViewById(R.id.focus_toolbar));
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
                    db_adapter.closeDB();
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
        activityFocusBinding.displayedTaskList.addItemDecoration(
                new DividerItemDecoration(
                        activityFocusBinding.displayedTaskList.getContext(),
                        DividerItemDecoration.VERTICAL)
        );
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
            case R.id.focus_how_to_use:
                display_search = false;
                goToHowToUse();
                return true;
            case R.id.focus_settings_item:
                Toast.makeText(this, "Focus Settings item selected", Toast.LENGTH_LONG)
                        .show();
                display_search = false;
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
        getSupportActionBar().setTitle(getString(R.string.module2_name) + " - Backlog");
        display_search = true;
        invalidateOptionsMenu();

        activityFocusBinding.addNewTaskBtn.setVisibility(View.VISIBLE);

        List<UncompTask> backlogTasks = db_adapter.getAllUncompTaskOfType(TaskType.BACKLOG);

        if(backlog_adapter == null)
            backlog_adapter = new BacklogTaskRVAdapter(null, db_adapter, this);
        backlog_adapter.setBacklogTasks(backlogTasks);

        activityFocusBinding.displayedTaskList.setAdapter(backlog_adapter);
    }

    /**
     * Displays the (up to) three active and three backburner tasks found in the "uncompleted_tasks"
     * table,
     */
    private void showActiveTasks(){
        getSupportActionBar().setTitle(getString(R.string.module2_name) + " - Priority & Optional");
        display_search = false;
        invalidateOptionsMenu();

        activityFocusBinding.addNewTaskBtn.setVisibility(View.GONE);
        List<UncompTask> activeTasks = db_adapter.getAllUncompTaskOfType(TaskType.PRIORITY);

        if(active_adapter == null)
            active_adapter = new ActiveTaskRVAdapter(null, db_adapter, this);
        active_adapter.setActiveTasks(activeTasks);

        activityFocusBinding.displayedTaskList.setAdapter(active_adapter);

        //TODO Display the optional tasks
    }

    private void showCompletedTasks(){
        getSupportActionBar().setTitle(getString(R.string.module2_name) + " - Completed");
        display_search = true;
        invalidateOptionsMenu();

        activityFocusBinding.addNewTaskBtn.setVisibility(View.GONE);

        List<CompTask> compTasks = db_adapter.getAllCompTask();

        if(comp_adapter == null)
            comp_adapter = new CompTaskRVAdapter(null);
        comp_adapter.setCompTasks(compTasks);

        activityFocusBinding.displayedTaskList.setAdapter(comp_adapter);
    }

    private void goToHowToUse(){
        startActivity(new Intent(this, HowToUseActivity.class));
    }

    public void goToTaskCreationPage(View v){
        startActivity(new Intent(this, AddTaskActivity.class));
    }


    // Stupid bullshit like this makes me dislike Android coding severely
    class pageGestureListener extends GestureDetector.SimpleOnGestureListener {
        private String TAG = "dumb_android_bullshit";

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d(TAG,"onDown: ");

            // don't return false here or else none of the other
            // gestures will work
            return true;
        }


//        @Override
//        public boolean onScroll(MotionEvent e1, MotionEvent e2,
//                                float distanceX, float distanceY) {
//            Log.i(TAG, "onScroll: ");
//            return true;
//        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Log.d(TAG, "onFling: " + velocityX + ", " + velocityY);
            switch (curr_page){
                case ACTIVE:
                    if (velocityX <= -3000){
                        showBacklog();
                        curr_page = TaskPage.BACKLOG;
                    }
                    break;
                case BACKLOG:
                    if (velocityX >= 3000){
                        showActiveTasks();
                        curr_page = TaskPage.ACTIVE;
                    }
                    else if(velocityX <= -3000){
                        showCompletedTasks();
                        curr_page = TaskPage.COMPLETED;
                    }
                    break;
                case COMPLETED:
                    if(velocityX >= 3000){
                        showBacklog();
                        curr_page = TaskPage.BACKLOG;
                    }
                    break;
            }

            return true;
        }
    }
}
