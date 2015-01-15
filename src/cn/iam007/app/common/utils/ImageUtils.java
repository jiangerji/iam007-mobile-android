package cn.iam007.app.common.utils;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import cn.iam007.app.common.cache.CacheConfiguration;
import cn.iam007.app.mall.widget.CircleBitmapDisplayer;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ImageUtils {

    public static void init(Context context) {
        // 设置图片的缓存的位置
        File cacheDir = CacheConfiguration.getExtCacheDirImage(context);
        File reserveCacheDir = CacheConfiguration.getInternalCacheDirImage(context);
        UnlimitedDiscCache discCache = new UnlimitedDiscCache(cacheDir,
                reserveCacheDir, new Md5FileNameGenerator());

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY + 1)
                .threadPoolSize(5).denyCacheImageMultipleSizesInMemory()
                .memoryCache(new WeakMemoryCache()).diskCache(discCache)
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    public static DisplayImageOptions getOptionsFadeIn() {
        return getOptionsFadeIn(250);
    }

    public static DisplayImageOptions getOptionsFadeIn(int millseconds) {
        return new DisplayImageOptions.Builder().cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
                .displayer(new FadeInBitmapDisplayer(millseconds))
                .imageScaleType(ImageScaleType.EXACTLY).build();
    }

    public static DisplayImageOptions getOptionsRound(int size) {
        return new DisplayImageOptions.Builder().cacheOnDisk(true)
                .cacheInMemory(true)
                .displayer(new RoundedBitmapDisplayer((int) (size)))
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
    }

    public static DisplayImageOptions getOptionCircle() {
        return new DisplayImageOptions.Builder().cacheOnDisk(true)
                .cacheInMemory(true)
                .displayer(new CircleBitmapDisplayer())
                .build();
    }

    /**
     * 构造图片url地址
     * 
     * @param imageId
     * @return
     */
    public static String buildImageUrl(String imageId) {
        String format = "http://iam007.cn/"
                + "media/tz_portfolio/article/cache/%s";

        try {
            int index = imageId.lastIndexOf('.');
            if (index >= 0) {
                imageId = imageId.substring(0, index) + "_M"
                        + imageId.substring(index);
            } else {
                imageId = imageId + "_M";
            }

            return String.format(format, imageId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 显示图片
     * 
     * @param imageId
     *            需要显示图片的id
     * @param imageView
     *            显示图片的对象
     */
    public static void showImage(
            String imageId, ImageView imageView) {
        ImageLoader.getInstance()
                .displayImage(buildImageUrl(imageId),
                        imageView, getOptionsFadeIn());
    }

    /**
     * 显示网络图片
     * 
     * @param imageUrl
     *            网络图片地址
     * @param imageView
     *            显示图片的对象
     * @param options
     *            显示图片的选项
     */
    public static void showImageByUrl(
            String imageUrl, ImageView imageView, DisplayImageOptions options) {
        ImageLoader.getInstance().displayImage(imageUrl, imageView, options);
    }
}
