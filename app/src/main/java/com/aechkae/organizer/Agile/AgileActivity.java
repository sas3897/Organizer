package com.aechkae.organizer.Agile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.aechkae.organizer.R;

import com.aechkae.organizer.Agile.ActiveTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AgileActivity extends AppCompatActivity {

    Button active_btn;
    Button backlog_btn;
    ListView backlogList;
    ListView activeList;

    //TODO Need to show which 'sprint' we're in, and maybe a prompt to choose for that

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agile);
        active_btn = findViewById(R.id.active_tab);
        backlog_btn = findViewById(R.id.backlog_tab);
        backlogList = findViewById(R.id.backlog_list);
        activeList = findViewById(R.id.active_list);

        //Most of the time you just want to see your active tasks right from the start
        showActive(active_btn);
    }

    public void showBacklog(View v){
        // Demarcate which tab is active
        active_btn.setBackgroundColor(0xFF3265AA);
        backlog_btn.setBackgroundColor(0xFFCCFFFF);

        //TODO Make this draw from a repository
        ArrayList<BacklogTask> backlogTasks = new ArrayList<>(Arrays.asList(
                new BacklogTask("FINSOA", "Finish writing the first draft of 'A Scarf of Arms'", 80),
                new BacklogTask("BACKUP", "Make a backup of the data on Hephaestus and Demeter", 0),
                new BacklogTask("ORGNZR", "Complete Organizer's calendar, agile, and alarm modules", 1),
                new BacklogTask("HIGHWY", "Get used to driving on the highway", 10),
                new BacklogTask("EDIBLE", "Bake some cookies/brownies", 20),
                new BacklogTask("BUDGET", "Budget out stuff, formalize procedure, etc", 40),
                new BacklogTask("EXRCIS", "Create a routine for exercising, including diet", 5),
                new BacklogTask("VGROTH", "Set up the Roth IRA with Vanguard and make the 2017/2018 contributions", 0),
                new BacklogTask("BONEST", "Write first draft of 'Bone Stronger than Bone'", 0),
                new BacklogTask("DRKNGT", "Write first draft of 'Dark was the Night'", 50)
        ));

        BacklogTaskAdapter backAdapter = new BacklogTaskAdapter(this, R.layout.backlog_task, backlogTasks);

        activeList.setAdapter(null);
        backlogList.setAdapter(backAdapter);
    }

    public void showActive(View v){
        // Demarcate which tab is active
        backlog_btn.setBackgroundColor(0xFF3265AA);
        active_btn.setBackgroundColor(0xFFCCFFFF);

        //TODO Make this draw from a repository
        ArrayList<ActiveTask> activeTasks = new ArrayList<>(Arrays.asList(
                new ActiveTask("test1", "testing this feature", 100),
                new ActiveTask("test2", "testing it again with a second one", 20)
        ));

        ActiveTaskAdapter activeAdapter = new ActiveTaskAdapter(this, R.layout.activelog_task, activeTasks);

        backlogList.setAdapter(null);
        activeList.setAdapter(activeAdapter);

        //TODO Display the back-burner tasks
    }

    public void completeTask(ActiveTask at){
        //TODO Move to 'log'
    }

    public void setPercentage(ActiveTask at, double newDone){
        at.percentageDone = newDone;
    }
}
