package server;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class DHMSLogFormatter extends Formatter {
    private Date date = new Date();
    public String format(LogRecord logger){
        date.setTime(logger.getMillis());
        return date+": "+logger.getMessage()+"\n";
    }
}