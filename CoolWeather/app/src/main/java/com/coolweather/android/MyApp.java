package com.coolweather.android;

import android.app.Application;
import android.os.Environment;

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
      /*  CoolWeatherLogger.getCoolWeatherLogger().setPath(Environment.getExternalStorageDirectory().getPath());
        HelpAppender.setLogLevel(true);*/

        MyLogger.getCoolWeatherLogger().init();
        HelpAppender.setLogLevel(true);
    }
}
