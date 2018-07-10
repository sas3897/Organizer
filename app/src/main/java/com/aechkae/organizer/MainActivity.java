package com.aechkae.organizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.aechkae.organizer.Agile.AgileActivity;
import com.aechkae.organizer.Timer.TimerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchAgile(View v){
        startActivity(new Intent(this, AgileActivity.class));
    }

    public void launchTimer(View v){
        startActivity(new Intent(this, TimerActivity.class));
    }
}
