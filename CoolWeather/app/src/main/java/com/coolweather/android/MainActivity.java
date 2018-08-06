package com.coolweather.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.coolweather.android.log.ConfigureLog4J;
import com.coolweather.android.log.MyLogger;
import com.coolweather.android.util.Main2Activity;


public class MainActivity extends AppCompatActivity {
    private  Switch sw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         sw = findViewById(R.id.sw);
        MyLogger.getLogger(MainActivity.class).error("MyLogger");
        MyLogger.getLogger(MainActivity.class).debug("MyLogger");
        MyLogger.getLogger(MainActivity.class).info("MyLogger");
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("isDebug",sw.isChecked());
                startActivity(intent);
            }
        });


    }
}
