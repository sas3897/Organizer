package com.aechkae.organizer.timer;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.aechkae.organizer.R;
import com.aechkae.organizer.checker.CheckerActivity;
import com.aechkae.organizer.databinding.ActivityTimerBinding;
import com.aechkae.organizer.focus.FocusActivity;
import com.aechkae.organizer.notable.NotableActivity;
import com.aechkae.organizer.reminder.ReminderActivity;

public class TimerActivity extends AppCompatActivity {

    private ActivityTimerBinding timerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timerBinding = DataBindingUtil.setContentView(this, R.layout.activity_timer);

        //Toolbar and Navbar
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        timerBinding.navBar.setNavigationItemSelectedListener((menuItem) -> {
            menuItem.setChecked(true);
            timerBinding.drawer.closeDrawers();

            //Swap the present activity
            switch(menuItem.getItemId()){
                case R.id.nav_focus: //We're in Focus already
                    startActivity(new Intent(this, FocusActivity.class));
                    break;
                case R.id.nav_reminder:
                    startActivity(new Intent(this, ReminderActivity.class));
                    break;
                case R.id.nav_timer:
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
                timerBinding.drawer.openDrawer(GravityCompat.START);
                return true;
            default:
                Toast.makeText(this, "Some item was pressed that this doesn't handle.", Toast.LENGTH_LONG)
                        .show();
                return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (timerBinding.drawer.isDrawerOpen(GravityCompat.START)) {
            timerBinding.drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
