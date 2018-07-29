package com.coolweather.android.log;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.Serializable;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Administrator on 2018/7/29.
 */

public class CustomDailyRollingFileAppender extends FileAppender {

    //初始化参数

    static final int TOP_OF_TROUBLE = -1;

    static final int TOP_OF_MINUTE = 0;

    static final int TOP_OF_HOUR = 1;

    static final int HALF_DAY = 2;

    static final int TOP_OF_DAY = 3;

    static final int TOP_OF_WEEK = 4;

    static final int TOP_OF_MONTH = 5;


    /**
     * 生产日志文件后缀（"'.'yyyy-MM-dd"）
     */

    private String datePattern = "'.'yyyy-MM-dd";

    /**
     * 默认：1个日志文件
     */

    protected int maxBackupIndex = 1;


    /**
     * 保存前一天的日志文件名称 =filename+("'.'yyyy-MM-dd")
     */

    private String scheduledFilename;


    //The next time we estimate a rollover should occur.

    private long nextCheck = System.currentTimeMillis() - 1;


    Date now = new Date();


    SimpleDateFormat sdf;


    RollingCalendar rc = new RollingCalendar();


    int checkPeriod = TOP_OF_TROUBLE;


    //The gmtTimeZone is used only in computeCheckPeriod() method.

    static final TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");


    //无参构造

    public CustomDailyRollingFileAppender() {

    }


    /**
     * 有参构造
     * <p>
     * Instantiate a<code>DailyRollingFileAppender</code> and open the
     * <p>
     * file designated by<code>filename</code>. The opened filename will
     * <p>
     * become the ouput destination for thisappender.
     */

    public CustomDailyRollingFileAppender(Layout layout, String filename,

                                          String datePattern) throws IOException {

        super(layout, filename, true);

        this.datePattern = datePattern;

        activateOptions();

    }


    public void setDatePattern(String pattern) {

        datePattern = pattern;

    }


    public void setMaxBackupIndex(int maxBackups) {

        this.maxBackupIndex = maxBackups;

    }


    public int getMaxBackupIndex() {

        return maxBackupIndex;

    }


    public String getDatePattern() {

        return datePattern;

    }


    @Override

    public void activateOptions() {

        super.activateOptions();

        if (datePattern != null && fileName != null) {

            now.setTime(System.currentTimeMillis());

            sdf = new SimpleDateFormat(datePattern);

            int type = computeCheckPeriod();

            printPeriodicity(type);

            rc.setType(type);

            File file = new File(fileName);

            scheduledFilename = fileName

                    + sdf.format(new Date(file.lastModified()));


        } else {

            LogLog.error("EitherFile or DatePattern options are not set for appender ["

                    + name + "].");

        }

    }


    void printPeriodicity(int type) {

        switch (type) {

            case TOP_OF_MINUTE:

                LogLog.debug("Appender[" + name + "] to be rolled every minute.");

                break;

            case TOP_OF_HOUR:

                LogLog.debug("Appender[" + name

                        + "] to be rolled on top of every hour.");

                break;

            case HALF_DAY:

                LogLog.debug("Appender[" + name

                        + "] to be rolled at midday and midnight.");

                break;

            case TOP_OF_DAY:

                LogLog.debug("Appender[" + name + "] to be rolled at midnight.");

                break;

            case TOP_OF_WEEK:

                LogLog.debug("Appender[" + name

                        + "] to be rolled at start of week.");

                break;

            case TOP_OF_MONTH:

                LogLog.debug("Appender[" + name

                        + "] to be rolled at start of every month.");

                break;

            default:

                LogLog.warn("Unknownperiodicity for appender [" + name + "].");

        }

    }


    //This method computes the roll over period by looping over the

    //periods, starting with the shortest, and stopping when the r0 is

    //different from from r1, where r0 is the epoch formatted according

    //the datePattern (supplied by the user) and r1 is the

    //epoch+nextMillis(i) formatted according to datePattern. All date

    //formatting is done in GMT and not local format because the test

    //logic is based on comparisons relative to 1970-01-01 00:00:00

    //GMT (the epoch).


    int computeCheckPeriod() {

        RollingCalendar rollingCalendar = new RollingCalendar(gmtTimeZone,

                Locale.getDefault());

        //set sate to 1970-01-01 00:00:00 GMT

        Date epoch = new Date(0);

        if (datePattern != null) {

            for (int i = TOP_OF_MINUTE; i <= TOP_OF_MONTH; i++) {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(

                        datePattern);

                simpleDateFormat.setTimeZone(gmtTimeZone);// do all date

                //formatting in GMT

                String r0 = simpleDateFormat.format(epoch);

                rollingCalendar.setType(i);

                Date next = new Date(rollingCalendar.getNextCheckMillis(epoch));

                String r1 = simpleDateFormat.format(next);

                //System.out.println("Type = "+i+", r0 = "+r0+", r1 ="+r1);

                if (r0 != null && r1 != null && !r0.equals(r1)) {

                    return i;

                }

            }

        }

        return TOP_OF_TROUBLE; // Deliberately head for trouble...

    }


    /**
     * 核心方法：生成日志文件，并完成对备份数量的监测以及历史日志的删除
     * <p>
     * Rollover the current file to a new file.
     */

    void rollOver() throws IOException {

        //获取所有日志历史文件：并完成对备份数量的监测以及历史日志的删除

        List<ModifiedTimeSortableFile> files = getAllFiles();

        Collections.sort(files);

        if (files.size() >= maxBackupIndex) {

            int index = 0;

            int diff = files.size() - (maxBackupIndex - 1);

            for (ModifiedTimeSortableFile file : files) {

                if (index >= diff)

                    break;


                file.delete();

                index++;

            }

        }



      /*Compute filename, but only if datePattern is specified */

        if (datePattern == null) {

            errorHandler.error("MissingDatePattern option in rollOver().");

            return;

        }

        LogLog.debug("maxBackupIndex=" + maxBackupIndex);


        String datedFilename = fileName + sdf.format(now);

        //It is too early to roll over because we are still within the

        //bounds of the current interval. Rollover will occur once the

        //next interval is reached.

        if (scheduledFilename.equals(datedFilename)) {

            return;

        }


        //close current file, and rename it to datedFilename

        this.closeFile();


        File target = new File(scheduledFilename);

        if (target.exists()) {

            target.delete();

        }


        File file = new File(fileName);

        boolean result = file.renameTo(target);

        if (result) {

            LogLog.debug(fileName + " -> " + scheduledFilename);

        } else {

            LogLog.error("Failedto rename [" + fileName + "] to ["

                    + scheduledFilename + "].");

        }


        try {

            //This will also close the file. This is OK since multiple

            //close operations are safe.

            this.setFile(fileName, true, this.bufferedIO, this.bufferSize);

        } catch (IOException e) {

            errorHandler.error("setFile(" + fileName + ", true) call failed.");

        }

        scheduledFilename = datedFilename;

    }


    /**
     * This method differentiatesDailyRollingFileAppender from its
     * <p>
     * super class.
     * <p>
     * <p>
     * <p>
     * <p>Before actually logging, thismethod will check whether it is
     * <p>
     * time to do a rollover. If it is, it willschedule the next
     * <p>
     * rollover time and then rollover.
     */

    @Override

    protected void subAppend(LoggingEvent event) {

        long n = System.currentTimeMillis();

        if (n >= nextCheck) {

            now.setTime(n);

            nextCheck = rc.getNextCheckMillis(now);

            try {

                rollOver();

            } catch (IOException ioe) {

                if (ioe instanceof InterruptedIOException) {

                    Thread.currentThread().interrupt();

                }

                LogLog.error("rollOver()failed.", ioe);

            }

        }

        super.subAppend(event);

    }


    /**
     * 获取同一项目日志目录下所有文件
     * <p>
     * This method searches list of log files
     * <p>
     * based on the pattern given in the log4jconfiguration file
     * <p>
     * and returns a collection
     *
     * @returnList&lt;ModifiedTimeSortableFile&gt;
     */

    private List<ModifiedTimeSortableFile> getAllFiles() {

        List<ModifiedTimeSortableFile> files = new ArrayList<>();

        FilenameFilter filter = new FilenameFilter() {

//重写accept方法，确认是否是同一项目日志文件名称前缀。

            @Override

            public boolean accept(File dir, String name) {

                String directoryName = dir.getPath();

                LogLog.debug("directoryname: " + directoryName);

                File file = new File(fileName);

                String perentDirectory = file.getParent();

                if (perentDirectory != null) {

                    //name=demo.log

                    //directoryName= /logs ，

                    //直接fileName.substring(directoryName.length())；切割之后的localFile=/demo.log…，会导致name.startsWith(localFile)始终是false

                    //so解决方案：长度 +1

                    String localFile = fileName.substring(directoryName

                            .length() + 1);

                    return name.startsWith(localFile);

                }

                return name.startsWith(fileName);

            }

        };

        File file = new File(fileName);

        String perentDirectory = file.getParent();

        if (file.exists()) {

            if (file.getParent() == null) {

                String absolutePath = file.getAbsolutePath();

                perentDirectory = absolutePath.substring(0,

                        absolutePath.lastIndexOf(fileName));


            }

        }

        File dir = new File(perentDirectory);

        String[] names = dir.list(filter);


        for (int i = 0; i < names.length; i++) {

            files.add(new ModifiedTimeSortableFile(dir

                    + System.getProperty("file.separator") + names[i]));

        }

        return files;

    }

}


/**
 * 自定义file类，重写compareTo方法，比较文件的修改时间
 * <p>
 * The ClassModifiedTimeSortableFile extends java.io.File class and
 * <p>
 * implements Comparable to sortfiles list based upon their modified date
 */

class ModifiedTimeSortableFile extends File implements Serializable,

        Comparable<File> {

    private static final long serialVersionUID = 1373373728209668895L;


    public ModifiedTimeSortableFile(String parent, String child) {

        super(parent, child);

        //TODO Auto-generated constructor stub

    }


    public ModifiedTimeSortableFile(URI uri) {

        super(uri);

        //TODO Auto-generated constructor stub

    }


    public ModifiedTimeSortableFile(File parent, String child) {

        super(parent, child);

    }


    public ModifiedTimeSortableFile(String string) {

        super(string);

    }


    @Override

    public int compareTo(File anotherPathName) {

        long thisVal = this.lastModified();

        long anotherVal = anotherPathName.lastModified();

        return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));

    }

}


/**
 * RollingCalendar is a helper class toDailyRollingFileAppender.
 * <p>
 * Given a periodicity type and the currenttime, it computes the
 * <p>
 * start of the next interval.
 */

class RollingCalendar extends GregorianCalendar {


    private static final long serialVersionUID = -8295691444111406775L;


    int type = CustomDailyRollingFileAppender.TOP_OF_TROUBLE;


    RollingCalendar() {

        super();

    }


    RollingCalendar(TimeZone tz, Locale locale) {

        super(tz, locale);

    }


    void setType(int type) {

        this.type = type;

    }


    public long getNextCheckMillis(Date now) {

        return getNextCheckDate(now).getTime();

    }


    public Date getNextCheckDate(Date now) {

        this.setTime(now);


        switch (type) {

            case CustomDailyRollingFileAppender.TOP_OF_MINUTE:

                this.set(Calendar.SECOND, 0);

                this.set(Calendar.MILLISECOND, 0);

                this.add(Calendar.MINUTE, 1);

                break;

            case CustomDailyRollingFileAppender.TOP_OF_HOUR:

                this.set(Calendar.MINUTE, 0);

                this.set(Calendar.SECOND, 0);

                this.set(Calendar.MILLISECOND, 0);

                this.add(Calendar.HOUR_OF_DAY, 1);

                break;

            case CustomDailyRollingFileAppender.HALF_DAY:

                this.set(Calendar.MINUTE, 0);

                this.set(Calendar.SECOND, 0);

                this.set(Calendar.MILLISECOND, 0);

                int hour = get(Calendar.HOUR_OF_DAY);

                if (hour < 12) {

                    this.set(Calendar.HOUR_OF_DAY, 12);

                } else {

                    this.set(Calendar.HOUR_OF_DAY, 0);

                    this.add(Calendar.DAY_OF_MONTH, 1);

                }

                break;

            case CustomDailyRollingFileAppender.TOP_OF_DAY:

                this.set(Calendar.HOUR_OF_DAY, 0);

                this.set(Calendar.MINUTE, 0);

                this.set(Calendar.SECOND, 0);

                this.set(Calendar.MILLISECOND, 0);

                this.add(Calendar.DATE, 1);

                break;

            case CustomDailyRollingFileAppender.TOP_OF_WEEK:

                this.set(Calendar.DAY_OF_WEEK, getFirstDayOfWeek());

                this.set(Calendar.HOUR_OF_DAY, 0);

                this.set(Calendar.MINUTE, 0);

                this.set(Calendar.SECOND, 0);

                this.set(Calendar.MILLISECOND, 0);

                this.add(Calendar.WEEK_OF_YEAR, 1);

                break;

            case CustomDailyRollingFileAppender.TOP_OF_MONTH:

                this.set(Calendar.DATE, 1);

                this.set(Calendar.HOUR_OF_DAY, 0);

                this.set(Calendar.MINUTE, 0);

                this.set(Calendar.SECOND, 0);

                this.set(Calendar.MILLISECOND, 0);

                this.add(Calendar.MONTH, 1);

                break;

            default:

                throw new IllegalStateException("Unknown periodicity type.");

        }

        return getTime();

    }


}
