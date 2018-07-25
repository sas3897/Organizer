package com.aechkae.organizer.focus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aechkae.organizer.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus_settings);
        setSupportActionBar(findViewById(R.id.focus_toolbar));
        getSupportActionBar().setTitle(R.string.module2_name);
    }
}
