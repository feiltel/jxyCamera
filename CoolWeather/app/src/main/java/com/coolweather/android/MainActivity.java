package com.coolweather.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

<<<<<<< HEAD
import com.coolweather.android.log.AppLogger;
=======
import com.coolweather.android.log.ConfigureLog4J;
>>>>>>> 5e0d116469f4f662643e24bf35b39eafe5991bfd
import com.coolweather.android.log.CoolWeatherLogger;
import com.coolweather.android.log.MyLogger;

import org.apache.log4j.Logger;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD
        CoolWeatherLogger.getCoolWeatherLogger().info("queryProvinces: ");
        CoolWeatherLogger.getCoolWeatherLogger().debug("queryProvinces: ");
        CoolWeatherLogger.getCoolWeatherLogger().error("queryProvinces: ");
        AppLogger.d("yyyyyy");
       // setContentView(R.layout.activity_main);
=======
      /*  CoolWeatherLogger.getLogger(MainActivity.class).info("queryProvinces ");
        CoolWeatherLogger.getLogger(MainActivity.class).debug("queryProvinces");
        CoolWeatherLogger.getLogger(MainActivity.class).error("queryProvinces ");*/


        MyLogger.getLogger(MainActivity.class).error("MyLogger");
        MyLogger.getLogger(MainActivity.class).debug("MyLogger");
        MyLogger.getLogger(MainActivity.class).info("MyLogger");
        // setContentView(R.layout.activity_main);

/*
        ConfigureLog4J configureLog4J = new ConfigureLog4J();
        configureLog4J.configure();
        //初始化 log
        Logger log = Logger.getLogger(this.getClass());
        //写 info 日志
        log.info("不知道呀就是测试一下啊");
        log.debug("不知道呀就是测试一下啊");
        log.error("不知道呀就是测试一下啊");*/
>>>>>>> 5e0d116469f4f662643e24bf35b39eafe5991bfd

    }
}
