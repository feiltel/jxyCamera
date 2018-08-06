package com.coolweather.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.coolweather.android.log.ConfigureLog4J;
import com.coolweather.android.log.CoolWeatherLogger;

import org.apache.log4j.Logger;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ConfigureLog4J configureLog4J=new ConfigureLog4J();
        configureLog4J.configure();
        //初始化 log

        Logger log=Logger.getLogger(this.getClass());
        //写 info 日志
        log.info("不知道呀就是测试一下啊");

    }
}
