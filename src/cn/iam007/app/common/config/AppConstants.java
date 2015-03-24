package cn.iam007.app.common.config;

public class AppConstants {

    public static boolean LOGGER_ENABLE = true;

    public static String HOST = "http://192.168.54.9:8000/";
    static {
        //        if (false)
        {
            HOST = "http://iam007.cn:801/";
        }
    }

    public static String ALIYUN_HOST = "http://123.57.77.122:802/";
    static {
        if (false)
        {
            ALIYUN_HOST = "http://192.168.54.9:8000/";
        }
    }

}
