package com.coolweather.android.util;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.coolweather.android.MainActivity;
import com.coolweather.android.R;
import com.coolweather.android.log.ConfigureLog4J;
import com.coolweather.android.log.MyLogger;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isDebug=getIntent().getBooleanExtra("isDebug",false);
        ConfigureLog4J.configure(isDebug,ConfigureLog4J.SURVEY_PATH);
        setContentView(R.layout.activity_main2);
        MyLogger.getLogger(MainActivity.class).error("Main2Activity");
        MyLogger.getLogger(MainActivity.class).debug("Main2Activity");
        MyLogger.getLogger(MainActivity.class).info("Main2Activity");
    }
}
