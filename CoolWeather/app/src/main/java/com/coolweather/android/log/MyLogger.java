package com.coolweather.android.log;

import android.os.Environment;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.util.Date;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * Created by Administrator on 2018/8/2.
 */

public class MyLogger extends Logger {
    private static MyLogger coolWeatherLogger;

    protected MyLogger(String name) {
        super(name);
        logger = Logger.getLogger(name);
    }
    public static synchronized MyLogger getLogger(Class myClass) {
        if (coolWeatherLogger == null) {
            coolWeatherLogger = new MyLogger(myClass.getName());
        }
        return coolWeatherLogger;
    }
    public static synchronized MyLogger getCoolWeatherLogger() {
        if (coolWeatherLogger == null) {
            coolWeatherLogger = new MyLogger("CoolWeatherLogger");
        }
        return coolWeatherLogger;
    }
    private Logger logger;

    private void setAppender( HelpAppender fileAppender) {
        if (fileAppender == null) {
            return;
        }
        logger.removeAllAppenders();
        logger.addAppender(fileAppender);
    }


    private HelpAppender debugLogerAppender;
    private HelpAppender infoLogerAppender;
    private HelpAppender errorLogerAppender;
    private String OPERATION_FORMAT = "%d %-5p [%c{2}]-[%L] %m%n";
    private String EXCEPTION_FORMAT = "%d %-5p [%c{2}]-[%L] %m%n";

    @Override
    public void debug(Object message) {
        if (message == null) {
            return;
        }
        setAppender(debugLogerAppender);
        configure(debugLogPath,OPERATION_FORMAT);
        logger.debug(message);
    }

    String rootPath = Environment.getExternalStorageDirectory().getPath();
    private String infoLogPath = rootPath + "/" + "log/common/info/info.log";
    private String errorLogPath = rootPath + "/" + "log/common/error/error.log";
    private String debugLogPath = rootPath + "/" + "log/common/debug/debug.log";

    public static void configure(String path,String format) {
        final LogConfigurator logConfigurator = new LogConfigurator();
        Date nowtime = new Date();
        // String needWriteMessage = myLogSdf.format(nowtime);
        //日志文件路径地址:SD卡下myc文件夹log文件夹的test文件
        String fileName = path ;
        //设置文件名
        logConfigurator.setFileName(fileName);
        //设置root日志输出级别 默认为DEBUG
        logConfigurator.setRootLevel(Level.DEBUG);
        // 设置日志输出级别
        logConfigurator.setLevel("org.apache", Level.INFO);
        //设置 输出到日志文件的文字格式 默认 %d %-5p [%c{2}]-[%L] %m%n
        logConfigurator.setFilePattern(format);
        //设置输出到控制台的文字格式 默认%m%n
        logConfigurator.setLogCatPattern(format);
        //设置总文件大小
        logConfigurator.setMaxFileSize(1024 * 1024 * 5);
        //设置最大产生的文件个数
        logConfigurator.setMaxBackupSize(7);
        //设置所有消息是否被立刻输出 默认为true,false 不输出
        logConfigurator.setImmediateFlush(true);
        //是否本地控制台打印输出 默认为true ，false不输出
        logConfigurator.setUseLogCatAppender(true);
        //设置是否启用文件附加,默认为true。false为覆盖文件
        logConfigurator.setUseFileAppender(true);
        //设置是否重置配置文件，默认为true
        logConfigurator.setResetConfiguration(true);
        //是否显示内部初始化日志,默认为false
        logConfigurator.setInternalDebugging(false);

        logConfigurator.configure();

    }

    @Override
    public void info(Object message) {
        if (message == null) {
            return;
        }
        setAppender(infoLogerAppender);
        configure(infoLogPath,OPERATION_FORMAT);
        logger.info(message);
    }

    @Override
    public void error(Object message) {
        if (message == null) {
            return;
        }
        setAppender(errorLogerAppender);
        setAppender( errorLogerAppender);
        configure(errorLogPath,EXCEPTION_FORMAT);
        logger.error(message);
    }

    @Override
    public void error(Object message, Throwable t) {
        if (message == null || t == null) {
            return;
        }
        setAppender( errorLogerAppender);
        logger.error(message);
        setAppender( errorLogerAppender);
        logger.error(message);
    }

    public void init() {
        this.infoLogerAppender = new HelpAppender(infoLogPath);
        this.errorLogerAppender = new HelpAppender(errorLogPath);
        this.debugLogerAppender = new HelpAppender(debugLogPath);
    }
}
