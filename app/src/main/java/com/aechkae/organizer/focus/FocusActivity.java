package com.aechkae.organizer.focus;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.aechkae.organizer.checker.CheckerActivity;
import com.aechkae.organizer.database.Schema;
import com.aechkae.organizer.focus.adapters.ActiveTaskRVAdapter;
import com.aechkae.organizer.focus.adapters.BacklogTaskRVAdapter;
import com.aechkae.organizer.focus.adapters.CompTaskRVAdapter;
import com.aechkae.organizer.focus.Task.TaskType;
import com.aechkae.organizer.R;
import com.aechkae.organizer.databinding.ActivityFocusBinding;
import com.aechkae.organizer.database.OrgDBAdapter;
import com.aechkae.organizer.notable.NotableActivity;
import com.aechkae.organizer.reminder.ReminderActivity;
import com.aechkae.organizer.timer.TimerActivity;

import java.util.ArrayList;
import java.util.List;

public class FocusActivity extends AppCompatActivity {

    public enum TaskPage{
        ACTIVE(Schema.UNCOMP_TASKS),
        BACKLOG(Schema.UNCOMP_TASKS),
        COMPLETED(Schema.COMP_TASKS);

        private Schema assoc_table;
        TaskPage(Schema table){
            assoc_table = table;
        }

        public Schema getAssoc_table() {
            return assoc_table;
        }
    }

    private ActivityFocusBinding focusBinding;
    private OrgDBAdapter db_adapter;
    private GestureDetectorCompat mDetector;

    private RecyclerView.Adapter<RecyclerView.ViewHolder> task_adapter;
    private CompTaskRVAdapter comp_adapter = null;
    private ActiveTaskRVAdapter active_adapter = null;
    private BacklogTaskRVAdapter backlog_adapter = null;

    private boolean searchable_and_clearable = false;
    private TaskPage curr_page;

    //TODO Need to show which 'sprint' we're in, and maybe a prompt to choose for that

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db_adapter = new OrgDBAdapter(this);  //TODO is this the proper context?
        db_adapter.openDB();
        focusBinding = DataBindingUtil.setContentView(this, R.layout.activity_focus);
        mDetector = new GestureDetectorCompat(this, new pageGestureListener());
        curr_page = TaskPage.ACTIVE;
        focusBinding.displayedTaskList.setOnTouchListener( (view, motion) -> mDetector.onTouchEvent(motion));

        //Toolbar and Navbar
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        focusBinding.navBar.setNavigationItemSelectedListener((menuItem) -> {
            menuItem.setChecked(true);
            focusBinding.drawer.closeDrawers();

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
                case R.id.nav_checker:
                    startActivity(new Intent(this, CheckerActivity.class));
                    break;
                default:
                    Toast.makeText(this, "Unknown nav item pressed", Toast.LENGTH_LONG)
                            .show();
            }

            return true;
        });

        //RecyclerView attributes
        focusBinding.displayedTaskList.addItemDecoration(
                new DividerItemDecoration(
                        focusBinding.displayedTaskList.getContext(),
                        DividerItemDecoration.VERTICAL)
        );
        focusBinding.displayedTaskList.setLayoutManager(new LinearLayoutManager(this));

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
            case R.id.focus_search:
                Toast.makeText(this, "Focus Search item selected", Toast.LENGTH_LONG)
                        .show();
                return true;
            case R.id.delete_all:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.focus_clear_tasks_prompt);
                builder.setCancelable(false);
                builder.setNegativeButton(R.string.cancel_action, (dialog, id) -> {});
                builder.setPositiveButton(R.string.confirm_deletion, (dialog, id) -> {
                    // Due to DB architecture this is currently worse named than it should be
                    switch(curr_page){
                        case BACKLOG:
                            db_adapter.clearTasks(curr_page.getAssoc_table(), true,
                                    new String[]{""+TaskType.BACKLOG.getDb_flag()});
                            backlog_adapter.setBacklogTasks(new ArrayList<>());
                            backlog_adapter.notifyDataSetChanged();
                            break;
                        case COMPLETED:
                            db_adapter.clearTasks(curr_page.getAssoc_table(), false, null);
                            comp_adapter.setCompTasks(new ArrayList<>());
                            comp_adapter.notifyDataSetChanged();
                            break;
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

                return true;
            //Module navigation menu button
            case android.R.id.home:
                focusBinding.drawer.openDrawer(GravityCompat.START);
                return true;
            default:
                Toast.makeText(this, "Some item was pressed that this doesn't handle.", Toast.LENGTH_LONG)
                        .show();
                return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (focusBinding.drawer.isDrawerOpen(GravityCompat.START)) {
            focusBinding.drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //Hide the search icon if necessary
        menu.findItem(R.id.focus_search).setVisible(searchable_and_clearable);
        menu.findItem(R.id.delete_all).setVisible(searchable_and_clearable);
        return super.onPrepareOptionsMenu(menu);
    }

    private void showBacklog(){
        getSupportActionBar().setTitle(getString(R.string.module2_name) + " - Backlog");
        searchable_and_clearable = true;
        invalidateOptionsMenu();

        focusBinding.addNewTaskBtn.setVisibility(View.VISIBLE);

        List<UncompTask> backlogTasks = db_adapter.getAllUncompTaskOfType(TaskType.BACKLOG);

        if(backlog_adapter == null)
            backlog_adapter = new BacklogTaskRVAdapter(null, db_adapter, this);
        backlog_adapter.setBacklogTasks(backlogTasks);

        focusBinding.displayedTaskList.setAdapter(backlog_adapter);
    }

    /**
     * Displays the (up to) three active and three backburner tasks found in the "uncompleted_tasks"
     * table,
     */
    private void showActiveTasks(){
        getSupportActionBar().setTitle(getString(R.string.module2_name) + " - Priority & Optional");
        searchable_and_clearable = false;
        invalidateOptionsMenu();

        focusBinding.addNewTaskBtn.setVisibility(View.GONE);
        List<UncompTask> activeTasks = db_adapter.getAllUncompTaskOfType(TaskType.PRIORITY);

        if(active_adapter == null)
            active_adapter = new ActiveTaskRVAdapter(null, db_adapter, this);
        active_adapter.setActiveTasks(activeTasks);

        focusBinding.displayedTaskList.setAdapter(active_adapter);

        //TODO Display the optional tasks
    }

    private void showCompletedTasks(){
        getSupportActionBar().setTitle(getString(R.string.module2_name) + " - Completed");
        searchable_and_clearable = true;
        invalidateOptionsMenu();

        focusBinding.addNewTaskBtn.setVisibility(View.GONE);

        List<CompTask> compTasks = db_adapter.getAllCompTask();

        if(comp_adapter == null)
            comp_adapter = new CompTaskRVAdapter(null);
        comp_adapter.setCompTasks(compTasks);

        focusBinding.displayedTaskList.setAdapter(comp_adapter);
    }

    private void goToHowToUse(){
        startActivity(new Intent(this, HowToUseActivity.class));
    }

    public void createNewTask(View v){
        startActivity(new Intent(this, AddTaskActivity.class));
    }


    // Stupid bullshit like this makes me dislike Android coding severely
    class pageGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
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
