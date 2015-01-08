package cn.iam007.utils.logging;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

import android.util.Log;

/**
 * Appender for {@link Log}
 * 
 * @author Rolf Kulemann, Pascal Bockhorn
 */
public class LogcatAppender extends AppenderSkeleton {

    protected Layout tagLayout;

    public LogcatAppender(final Layout messageLayout, final Layout tagLayout) {
        this.tagLayout = tagLayout;
        setLayout(messageLayout);
    }

    public LogcatAppender(final Layout messageLayout) {
        this(messageLayout, new PatternLayout("%c"));
    }

    public LogcatAppender() {
        this(new PatternLayout("%m%n"));
    }

    @Override
    protected void append(final LoggingEvent le) {
        switch (le.getLevel().toInt()) {
        case Level.TRACE_INT:
            if (le.getThrowableInformation() != null) {
                Log.v(getTagLayout().format(le),
                        getLayout().format(le),
                        le.getThrowableInformation().getThrowable());
            }
            else {
                Log.v(getTagLayout().format(le), getLayout().format(le));
            }
            break;
        case Level.DEBUG_INT:
            if (le.getThrowableInformation() != null) {
                Log.d(getTagLayout().format(le),
                        getLayout().format(le),
                        le.getThrowableInformation().getThrowable());
            }
            else {
                Log.d(getTagLayout().format(le), getLayout().format(le));
            }
            break;
        case Level.INFO_INT:
            if (le.getThrowableInformation() != null) {
                Log.i(getTagLayout().format(le),
                        getLayout().format(le),
                        le.getThrowableInformation().getThrowable());
            }
            else {
                Log.i(getTagLayout().format(le), getLayout().format(le));
            }
            break;
        case Level.WARN_INT:
            if (le.getThrowableInformation() != null) {
                Log.w(getTagLayout().format(le),
                        getLayout().format(le),
                        le.getThrowableInformation().getThrowable());
            }
            else {
                Log.w(getTagLayout().format(le), getLayout().format(le));
            }
            break;
        case Level.ERROR_INT:
            if (le.getThrowableInformation() != null) {
                Log.e(getTagLayout().format(le),
                        getLayout().format(le),
                        le.getThrowableInformation().getThrowable());
            }
            else {
                Log.e(getTagLayout().format(le), getLayout().format(le));
            }
            break;
        case Level.FATAL_INT:
            if (le.getThrowableInformation() != null) {
                Log.wtf(getTagLayout().format(le),
                        getLayout().format(le),
                        le.getThrowableInformation().getThrowable());
            }
            else {
                Log.wtf(getTagLayout().format(le), getLayout().format(le));
            }
            break;
        }
    }

    @Override
    public void close() {
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }

    public Layout getTagLayout() {
        return tagLayout;
    }

    public void setTagLayout(final Layout tagLayout) {
        this.tagLayout = tagLayout;
    }
}
