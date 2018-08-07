
package com.nutstudio.mylogger.logutils;

import org.apache.log4j.Logger;

public class Log {
    public static synchronized Logger getLogger(Class myClass) {
        return Logger.getLogger(myClass.getName());
    }

}
