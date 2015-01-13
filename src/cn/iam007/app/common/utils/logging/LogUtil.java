package cn.iam007.app.common.utils.logging;

import org.apache.log4j.Logger;

import android.util.Log;
import cn.iam007.app.common.config.AppConstants;

public class LogUtil {

    public static Logger getLogger() {
        return Logger.getLogger("main");
    }

    public static Logger getLogger(String name) {
        if (name == null || name.length() == 0) {
            name = "main";
        }
        return Logger.getLogger(name);
    }

    public static void d(String msg) {
        if (AppConstants.LOGGER_ENABLE) {
            StackTraceElement[] elements = Thread.currentThread()
                    .getStackTrace();
            String tag = elements[1].getClass().getName();

            d(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (AppConstants.LOGGER_ENABLE) {
            Log.d(tag, msg);
        }
    }

    public static void v(String msg) {
        if (AppConstants.LOGGER_ENABLE) {
            StackTraceElement[] elements = Thread.currentThread()
                    .getStackTrace();
            String tag = elements[1].getClass().getName();

            v(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (AppConstants.LOGGER_ENABLE) {
            Log.v(tag, msg);
        }
    }

    public static void e(String msg) {
        if (AppConstants.LOGGER_ENABLE) {
            StackTraceElement[] elements = Thread.currentThread()
                    .getStackTrace();
            String tag = elements[1].getClass().getName();

            e(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (AppConstants.LOGGER_ENABLE) {
            Log.e(tag, msg);
        }
    }
}
