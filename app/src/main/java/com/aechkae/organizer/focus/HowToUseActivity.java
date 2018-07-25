package com.aechkae.organizer.focus;

import android.content.res.AssetManager;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.aechkae.organizer.R;
import com.aechkae.organizer.databinding.ActivityHowToUseBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HowToUseActivity extends AppCompatActivity {

    ActivityHowToUseBinding activityHowToUseBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHowToUseBinding = DataBindingUtil.setContentView(this, R.layout.activity_how_to_use);
        setSupportActionBar(findViewById(R.id.focus_toolbar));
        getSupportActionBar().setTitle(R.string.module2_name);

        AssetManager assetManager = getAssets();
        try {
            InputStream is = assetManager.open("focus_how_to_use.txt");
            BufferedReader usageRdr = new BufferedReader(new InputStreamReader(is));
            String str;
            StringBuilder resultStr = new StringBuilder();
            while((str = usageRdr.readLine()) != null){
                resultStr.append(str);
            }
            is.close();

            activityHowToUseBinding.howToUseText.setText(resultStr.toString().replaceAll("`", "\n"));
        }
        catch (IOException io){
            Toast.makeText(this, "Hey we didn't find the file", Toast.LENGTH_LONG)
                    .show();
        }
    }
}
