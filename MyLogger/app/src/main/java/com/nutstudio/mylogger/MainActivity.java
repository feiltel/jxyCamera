package com.nutstudio.mylogger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nutstudio.mylogger.logutils.Log;
import com.nutstudio.mylogger.logutils.Log4jConfigure;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log4jConfigure.configure();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.getLogger(MainActivity.class).debug("MainActivity1");
        Log.getLogger(MainActivity.class).error("MainActivity1");
        Log.getLogger(MainActivity.class).info("MainActivity1");

        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log4jConfigure.NOW_PATH=Log4jConfigure.SURVEY_PATH;
                Log.getLogger(MainActivity.class).debug("MainActivity1");
                Log.getLogger(MainActivity.class).error("MainActivity1");
                Log.getLogger(MainActivity.class).info("MainActivity1");
            }
        });
    }
}
