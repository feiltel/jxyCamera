
package com.nutstudio.mylogger.logutils;

import org.apache.log4j.Logger;

public class Log {
    public static synchronized Logger getLogger(Class myClass) {
        Log4jConfigure.configure(true, Log4jConfigure.NOW_PATH);
        return Logger.getLogger(myClass.getName());
    }


}
