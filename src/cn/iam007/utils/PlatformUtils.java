package cn.iam007.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import cn.iam007.IAM007Application;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class PlatformUtils {
    private static int mScreenWidth = 0;
    private static int mScreenHeight = 0;
    private static float mDensity = 0;

    public static float getDensity(Context context) {
        if (mDensity > 0) {
            return mDensity;
        }

        if (context == null) {
            context = IAM007Application.getCurrentApplication();
        }

        DisplayMetrics dm = new DisplayMetrics();
        // 取得窗口属性
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(dm);

        // 窗口的宽度
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        mDensity = dm.density;

        return mDensity;
    }

    /**
     * 获取屏幕宽度，单位像素
     * 
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        if (mScreenWidth > 0) {
            return mScreenWidth;
        }

        if (context == null) {
            context = IAM007Application.getCurrentApplication();
        }

        DisplayMetrics dm = new DisplayMetrics();
        // 取得窗口属性
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(dm);

        // 窗口的宽度
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        mDensity = dm.density;

        return mScreenWidth;
    }

    /**
     * 获取屏幕宽度，单位像素
     * 
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        if (mScreenHeight > 0) {
            return mScreenHeight;
        }

        if (context == null) {
            context = IAM007Application.getCurrentApplication();
        }

        DisplayMetrics dm = new DisplayMetrics();
        // 取得窗口属性
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(dm);

        // 窗口的宽度
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        mDensity = dm.density;

        return mScreenHeight;
    }

    /**
     * 获取当前应用的version code
     * 
     * @param context
     * @return
     *         返回应用的version code, 如果为-1, 表示获取时发生异常
     */
    public static int getVersionCode(Context context) {
        //获取版本号(内部识别号)
        try {
            PackageInfo pi = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (NameNotFoundException e) {
            return -1;
        }
    }

    /**
     * 检查当前应用是否有新的版本
     * 
     * @param context
     * @param callBack
     *            检查新版本的回调函数
     */
    public static void checkUpdate(
            Context context, RequestCallBack<String> callBack) {
        String action = "version";
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("type", "android");
        params.addQueryStringParameter("version", "" + getVersionCode(context));
        CommonHttpUtils.get(action,
                params,
                callBack,
                "cache.check.update",
                30 * 60);
    }

}
