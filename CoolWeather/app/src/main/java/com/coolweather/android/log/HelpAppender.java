package com.coolweather.android.log;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2018/8/2.
 */

public class HelpAppender extends AppenderSkeleton {
    private String filePath;
    private CustomDailyRollingFileAppender fileAppender;
    private Logger logger = Logger.getLogger(HelpAppender.class);
    private String datePattern = "'.'yyyy-MM-d";
    private ConsoleAppender consoleAppender;

    public HelpAppender(String path) {
        this.filePath = path;
        layout = new PatternLayout();
    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        if (check()) {
            return;
        }
        if (loggingEvent.getMessage() == null) {
            return;
        }
        try {
            fileAppender = new CustomDailyRollingFileAppender(layout, filePath, datePattern);
            consoleAppender = new ConsoleAppender(layout);
            if (loggingEvent.getLevel() == Level.ERROR) {
                consoleAppender.setTarget(ConsoleAppender.SYSTEM_ERR);
            }
            doInfo(loggingEvent.getMessage().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doInfo(String string) {
        logger.setAdditivity(false);
        logger.removeAllAppenders();
        logger.addAppender(consoleAppender);
        logger.addAppender(fileAppender);
        logger.info(string);

    }

    private boolean check() {
        if (filePath.isEmpty()) {
            return true;
        }
        return !createFolder(new File(filePath).getParentFile());
    }

    private boolean createFolder(File parentFile) {
        return parentFile.isDirectory() || parentFile.mkdirs();
    }

    public static void setLogLevel(boolean isDebug) {
        if (isDebug) {
            Level level = Level.toLevel(String.valueOf(Level.DEBUG));
            LogManager.getRootLogger().setLevel(level);
        } else {
            Level level = Level.toLevel(String.valueOf(Level.INFO));
            LogManager.getRootLogger().setLevel(level);
        }
    }

    @Override
    public void close() {

    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
