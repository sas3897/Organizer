package com.aechkae.organizer.reminder;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.aechkae.organizer.R;
import com.aechkae.organizer.checker.CheckerActivity;
import com.aechkae.organizer.database.OrgDBAdapter;
import com.aechkae.organizer.databinding.ActivityReminderBinding;
import com.aechkae.organizer.focus.FocusActivity;
import com.aechkae.organizer.notable.NotableActivity;
import com.aechkae.organizer.timer.TimerActivity;

public class ReminderActivity extends AppCompatActivity {

    private ActivityReminderBinding reminderBinding;
    private OrgDBAdapter db_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db_adapter = new OrgDBAdapter(this);  //TODO is this the proper context?
        db_adapter.openDB();
        reminderBinding = DataBindingUtil.setContentView(this, R.layout.activity_reminder);

        //Toolbar and Navbar
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        reminderBinding.navBar.setNavigationItemSelectedListener((menuItem) -> {
            menuItem.setChecked(true);
            reminderBinding.drawer.closeDrawers();

            //Swap the present activity
            switch(menuItem.getItemId()){
                case R.id.nav_focus: //We're in Focus already
                    startActivity(new Intent(this, FocusActivity.class));
                    break;
                case R.id.nav_reminder:
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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            //Module navigation menu button
            case android.R.id.home:
                reminderBinding.drawer.openDrawer(GravityCompat.START);
                return true;
            default:
                Toast.makeText(this, "Some item was pressed that this doesn't handle.", Toast.LENGTH_LONG)
                        .show();
                return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (reminderBinding.drawer.isDrawerOpen(GravityCompat.START)) {
            reminderBinding.drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
