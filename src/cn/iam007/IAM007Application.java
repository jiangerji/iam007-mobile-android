package cn.iam007;

import java.io.File;

import org.apache.log4j.Level;

import android.app.Application;
import android.os.Environment;
import android.text.TextUtils;
import cn.iam007.common.utils.ImageUtils;
import cn.iam007.common.utils.PlatformUtils;
import cn.iam007.common.utils.logging.LogConfigurator;

import com.baidu.mobstat.StatService;

public class IAM007Application extends Application {

    private static IAM007Application mIam007Application;

    @Override
    public void onCreate() {
        super.onCreate();

        mIam007Application = this;

        logConfigure();

        ImageUtils.init(this);

        // 初始化百度统计
        String appChannel = PlatformUtils.getMeteDataByKey(this, "app_channel");
        if (TextUtils.isEmpty(appChannel)) {
            appChannel = "offical";
        }
        StatService.setAppChannel(this, appChannel, true);
        StatService.setSessionTimeOut(30);
    }

    public final static IAM007Application getCurrentApplication() {
        return mIam007Application;
    }

    public static void logConfigure() {
        final LogConfigurator logConfigurator = new LogConfigurator();

        File file = new File(Environment.getExternalStorageDirectory(),
                ".iam007");
        if (!file.exists()) {
            file.mkdirs();
        }

        logConfigurator.setFileName(new File(file, "app.log").getAbsolutePath());
        logConfigurator.setRootLevel(Level.DEBUG);
        // Set log level of a specific logger
        logConfigurator.configure();
    }
}
