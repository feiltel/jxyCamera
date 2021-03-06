
package com.nutstudio.mylogger.logutils;

import android.os.Environment;

import com.nutstudio.mylogger.log.LogConfigurator;

import org.apache.log4j.Level;

import java.io.File;


public class Log4jConfigure {
    public static String NOW_PATH="log/common/info.log";
    public static final String SURVEY_PATH = "log/survey/info.log";
    public static final String COMMON_PATH = "log/common/info.log";
    private static final String TAG = "Log4jConfigure";
    // 对应AndroidManifest文件中的package
    public static void configure(boolean isDebug, String filePath) {
        final LogConfigurator logConfigurator = new LogConfigurator();
        try {
            if (isSdcardMounted()) {
                logConfigurator.setFileName(Environment.getExternalStorageDirectory()
                        + File.separator + filePath);
            }
            //以下设置是按指定大小来生成新的文件
            /*
             * logConfigurator.setMaxBackupSize(4);
             * logConfigurator.setMaxFileSize(MAX_FILE_SIZE);
             */

            //以下设置是按天生成新的日志文件,与以上两句互斥,MAX_FILE_SIZE将不在起作用
            //文件名形如 MyApp.log.2016-06-02,MyApp.log.2016-06-03
            logConfigurator.setUseDailyRollingFileAppender(true);

            //以下为通用配置
            logConfigurator.setImmediateFlush(true);
            logConfigurator.setRootLevel(isDebug ? Level.DEBUG : Level.INFO);
            logConfigurator.setFilePattern("[%d] [%l] [%p] : %m%n");
            //设置输出到控制台的文字格式 默认%m%n
            logConfigurator.setLogCatPattern("[%d] [%l] [%p] : %m%n");

            logConfigurator.configure();
            android.util.Log.e(TAG, "Log4j config finish");
        } catch (Throwable throwable) {
            logConfigurator.setResetConfiguration(true);
            android.util.Log.e(TAG, "Log4j config error, use default config. Error:" + throwable);
        }
    }

    public static void configure() {
        configure(false, COMMON_PATH);
    }

    private static boolean isSdcardMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }
}
