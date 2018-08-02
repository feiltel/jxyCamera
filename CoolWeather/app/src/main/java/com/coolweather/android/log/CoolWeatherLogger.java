package com.coolweather.android.log;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * Created by Administrator on 2018/8/2.
 */

public class CoolWeatherLogger extends Logger {
    private static final String PATTERN = "[%s][%s][%s]:%s%n";
    private static CoolWeatherLogger coolWeatherLogger;
    private static String className;
    private String module;
    private String feature;
    private String sdPath;

    protected CoolWeatherLogger(String name) {
        super(name);
        this.module=name;
        this.feature=name;
        logger=Logger.getLogger(name);
    }
    public Logger getLogger(){
        return getLogger("CoolWeatherLogger");
    }
    public static synchronized CoolWeatherLogger getLogger(String name){
        if (coolWeatherLogger==null){
            coolWeatherLogger=new CoolWeatherLogger(name);
        }
        className=name;
        return coolWeatherLogger;
    }
    public static synchronized CoolWeatherLogger getCoolWeatherLogger(){
        if (coolWeatherLogger==null){
            coolWeatherLogger=new CoolWeatherLogger("CoolWeatherLogger");
        }
        return coolWeatherLogger;
    }
    private PatternLayout patternLayout;
    private Logger logger;
    private void  setAppender(String format,HelpAppender fileAppender ){
        if (fileAppender==null||this.patternLayout==null){
            return;
        }
        logger.removeAllAppenders();
        patternLayout.setConversionPattern(format);
        logger.addAppender(fileAppender);

    }
    private String OPERATION_FORMAT="%d%p%m";
    private String EXCEPTION_FORMAT="%d%p%m";
    private HelpAppender debugLogerAppender;
    private HelpAppender infoLogerAppender;
    private HelpAppender errorLogerAppender;
    @Override
    public void debug(Object message) {
        if (message==null){
            return;
        }
        setAppender(OPERATION_FORMAT,debugLogerAppender);
        logger.debug(newMessage(message));
    }

    private String newMessage(Object message) {
        String newMsg=message.toString();
        if (message instanceof Throwable){
            newMsg =((Throwable) message).getMessage();
        }
        return String.format(PATTERN,module,feature,className,newMsg);
    }
    private String newMessage(Throwable message) {
        return message.getMessage();
    }
    @Override
    public void info(Object message) {
        if (message==null){
            return;
        }
        setAppender(OPERATION_FORMAT,infoLogerAppender);
        logger.info(newMessage(message));
    }

    @Override
    public void error(Object message) {
        if (message==null){
            return;
        }
        setAppender(EXCEPTION_FORMAT,errorLogerAppender);
        String newMsg=message.toString();
        if (message instanceof Throwable){
            newMsg=newMessage((Throwable)message);

        }
        setAppender(OPERATION_FORMAT,errorLogerAppender);

        logger.error(String.format(PATTERN,module,feature,className,newMsg));
    }

    @Override
    public void error(Object message, Throwable t) {
        if (message==null||t==null){

            return;
        }
        setAppender(OPERATION_FORMAT,errorLogerAppender);
        logger.error(newMessage(message+": "+t.getMessage()));
        setAppender(EXCEPTION_FORMAT,errorLogerAppender);
        logger.error(newMessage(message+newMessage(t)));

    }
    public void setPath(String sdPath){
        this.sdPath=sdPath;
        String infoLogPath=sdPath+"/"+"log/common/info/info.log";
        String errorLogPath=sdPath+"/"+"log/common/error/error.log";
        String debugLogPath=sdPath+"/"+"log/common/debug/debug.log";
        this.infoLogerAppender=new HelpAppender(infoLogPath);
        this.errorLogerAppender=new HelpAppender(errorLogPath);
        this.debugLogerAppender=new HelpAppender(debugLogPath);
        this.patternLayout=new PatternLayout();

    }
}
