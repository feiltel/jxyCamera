package com.coolweather.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.coolweather.android.log.CoolWeatherLogger;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoolWeatherLogger.getCoolWeatherLogger().info("queryProvinces: ");
        CoolWeatherLogger.getCoolWeatherLogger().debug("queryProvinces: ");
        CoolWeatherLogger.getCoolWeatherLogger().error("queryProvinces: ");
       // setContentView(R.layout.activity_main);

    }
}
