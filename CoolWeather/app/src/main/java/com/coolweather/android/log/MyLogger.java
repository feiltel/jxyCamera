package com.coolweather.android.log;

import android.os.Environment;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Date;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * Created by Administrator on 2018/8/2.
 */

public class MyLogger {
    private static Logger logger;
    private static String rootPath = Environment.getExternalStorageDirectory().getPath();
    private static String infoLog = rootPath + "/" + "log/common/info.log";
    public static synchronized Logger getLogger(Class myClass) {
        logger = Logger.getLogger(myClass);
        logger.removeAllAppenders();
        logger.addAppender(new HelpAppender(infoLog));
        return logger;
    }








}
