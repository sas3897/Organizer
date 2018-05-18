package com.aechkae.organizer.Agile;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.aechkae.organizer.R;

import java.util.ArrayList;
import java.util.Arrays;


public class AgileActivity extends AppCompatActivity {

    Button active_btn;
    Button backlog_btn;
    ListView backlogList;
    ListView activeList;
    SQLiteDatabase taskDatabase;

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
        taskDatabase = openOrCreateDatabase(getResources().getString(R.string.database_name), MODE_PRIVATE, null);
    }

    public void showBacklog(View v){
        // Demarcate which tab is active
        active_btn.setBackgroundColor(0xFF3265AA);
        backlog_btn.setBackgroundColor(0xFFCCFFFF);
        //TODO add a bevel depression to the backlog button

        //TODO Make this draw from a repository
        ArrayList<Task> backlogTasks = new ArrayList<>(Arrays.asList(
                new Task("FINSOA", "Finish writing the first draft of 'A Scarf of Arms'", 80),
                new Task("BACKUP", "Make a backup of the data on Hephaestus and Demeter", 0),
                new Task("ORGNZR", "Complete Organizer's calendar, agile, and alarm modules", 1),
                new Task("HIGHWY", "Get used to driving on the highway", 10),
                new Task("EDIBLE", "Bake some cookies/brownies", 20),
                new Task("BUDGET", "Budget out stuff, formalize procedure, etc", 40),
                new Task("EXRCIS", "Create a routine for exercising, including diet", 5),
                new Task("VGROTH", "Set up the Roth IRA with Vanguard and make the 2017/2018 contributions", 0),
                new Task("BONEST", "Write first draft of 'Bone Stronger than Bone'", 0),
                new Task("DRKNGT", "Write first draft of 'Dark was the Night'", 50)
        ));

        BacklogTaskAdapter backAdapter = new BacklogTaskAdapter(this, R.layout.backlog_task, backlogTasks);

        activeList.setAdapter(null);
        backlogList.setAdapter(backAdapter);
    }

    public void showActive(View v){
        // Demarcate which tab is active
        backlog_btn.setBackgroundColor(0xFF3265AA);
        active_btn.setBackgroundColor(0xFFCCFFFF);
        //TODO add a bevel depression to the active button


        //TODO Make this draw from a repository
        ArrayList<Task> activeTasks = new ArrayList<>(Arrays.asList(
                new Task("test1", "testing this feature", 100),
                new Task("test2", "testing it again with a second one", 20)
        ));

        ActiveTaskAdapter activeAdapter = new ActiveTaskAdapter(this, R.layout.activelog_task, activeTasks);

        backlogList.setAdapter(null);
        activeList.setAdapter(activeAdapter);

        //TODO Display the back-burner tasks
    }

    public void completeTask(Task at){
        //TODO Move to 'log'
    }
}
