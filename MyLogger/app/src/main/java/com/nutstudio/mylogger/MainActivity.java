package com.nutstudio.mylogger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.nutstudio.mylogger.logutils.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("yyy","MainActivity");
                Log.d("yyy","MainActivity");
                Log.e("yyy","MainActivity");
                Log.getLogger(MainActivity.class).debug("MainActivity");
            }
        });
    }
}
