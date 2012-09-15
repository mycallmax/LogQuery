import java.io.*;
import java.util.Date;
import java.util.logging.*;

// This custom formatter formats parts of a log record to a single line
class MyLogFormatter extends Formatter {
    // This method is called for every log records
    public String format(LogRecord rec) {
        StringBuffer buf = new StringBuffer(1000);
		    buf.append(rec.getLevel());
        buf.append(": ");
        buf.append(formatMessage(rec));
        buf.append(' ');
        buf.append(new Date());
        buf.append('\n');
        return buf.toString();
    }
}
