package com.coolweather.android;

import android.app.Application;
import android.os.Environment;

import com.coolweather.android.log.ConfigureLog4J;
import com.coolweather.android.log.CoolWeatherLogger;
import com.coolweather.android.log.HelpAppender;
import com.coolweather.android.log.MyLogger;

/**
 * Created by Administrator on 2018/8/2.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ConfigureLog4J.configure(true,ConfigureLog4J.COMMON_PATH);
    }
}
