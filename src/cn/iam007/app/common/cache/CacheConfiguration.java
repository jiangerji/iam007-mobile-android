package cn.iam007.app.common.cache;

import java.io.File;

import android.content.Context;
import cn.iam007.app.mall.IAM007Application;

import com.nostra13.universalimageloader.utils.StorageUtils;

public class CacheConfiguration {
    private final static String CACHE_DIR_IMAGE = "image";

    /**
     * 获取应用外部图片缓存地址
     * 
     * @param context
     * @return
     */
    public static File getExtCacheDirImage(Context context) {
        File file = StorageUtils.getCacheDirectory(context);

        return new File(file, CACHE_DIR_IMAGE);
    }

    /**
     * 获取应用内部私有图片缓存地址
     * 
     * @param context
     * @return
     */
    public static File getInternalCacheDirImage(Context context) {
        File file = StorageUtils.getCacheDirectory(context, false);

        return new File(file, CACHE_DIR_IMAGE);
    }

    private final static String CACHE_DIR_HTTP = "json";

    /**
     * 获取网络请求返回结果的缓存目录
     * 
     * @param context
     * @return
     */
    public static File getExtCacheDirHttp(Context context) {
        if (context == null) {
            context = IAM007Application.getCurrentApplication();
        }

        File cacheFile = new File(StorageUtils.getCacheDirectory(context),
                CACHE_DIR_HTTP);
        if (!cacheFile.isDirectory()) {
            cacheFile.mkdirs();
        }

        return cacheFile;
    }

    private final static String CACHE_DIR_PLUGIN = "plugin";

    public static File getCacheDirPlugin() {
        Context context = IAM007Application.getCurrentApplication();

        File cacheFile = new File(context.getFilesDir(), CACHE_DIR_PLUGIN);
        if (!cacheFile.isDirectory()) {
            cacheFile.mkdirs();
        }

        return cacheFile;
    }
}
