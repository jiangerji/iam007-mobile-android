package cn.iam007.app.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.NameValuePair;

import android.text.TextUtils;
import android.webkit.URLUtil;
import cn.iam007.app.common.cache.CacheConfiguration;
import cn.iam007.app.common.config.AppConstants;
import cn.iam007.app.common.exception.HttpExceptionButFoundCache;
import cn.iam007.app.common.utils.logging.LogUtil;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.cache.MD5FileNameGenerator;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class CommonHttpUtils {
    private final static String TAG = "CommonHttpUtils";

    private final static String BASE_URL = AppConstants.HOST + "iam007/apis/";

    private static void printParams(RequestParams params) {
        if (params != null) {
            for (NameValuePair nameValuePair : params.getQueryStringParams()) {
                LogUtil.d(TAG,
                        "  " + nameValuePair.getName() + ":"
                                + nameValuePair.getValue());
            }
        }

    }

    public static void get(
            final String action, final RequestParams params,
            final RequestCallBack<String> callBack) {
        get(action, params, callBack, null);
    }

    public static void get(final String action, final RequestParams params,
            final RequestCallBack<String> callBack, final String cacheKey) {
        get(action, params, callBack, cacheKey, 10);
    }

    /**
     * 请求网络数据
     * 
     * @param action
     *            请求方法
     * @param params
     *            请求参数
     * @param callBack
     *            请求完成回调
     * @param cacheKey
     *            如果不为空，会将数据进行缓存，当网络出现异常，会直接返回cache数据
     *            如果为空，则不会进行缓存，当网络出现异常，会返回异常给调用者
     *            单位：秒
     */
    public static void get(
            final String action, final RequestParams params,
            final RequestCallBack<String> callBack, final String cacheKey,
            final int cacheExpiry) {
        String tempUrl = action;
        if (!URLUtil.isNetworkUrl(action)) {
            tempUrl = BASE_URL + action;
        }
        final String url = tempUrl;
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000 * cacheExpiry);
        http.configSoTimeout(1000 * 10);
        http.configTimeout(1000 * 10);
        http.send(HttpRequest.HttpMethod.GET,
                url,
                params,
                new RequestCallBack<String>() {
                    MD5FileNameGenerator md5 = new MD5FileNameGenerator();

                    @Override
                    public void onLoading(
                            long total, long current, boolean isUploading) {
                        if (callBack != null) {
                            callBack.onLoading(total, current, isUploading);
                        }
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtil.d(TAG, "Get " + url);
                        printParams(params);
                        LogUtil.d(TAG, "onSuccess:" + responseInfo.result);
                        if (callBack != null) {
                            callBack.onSuccess(responseInfo);
                        }

                        try {
                            if (!TextUtils.isEmpty(cacheKey)) {
                                String cacheName = md5.generate(cacheKey);

                                File file = new File(CacheConfiguration.getExtCacheDirHttp(null),
                                        cacheName);
                                FileOutputStream fos = new FileOutputStream(file);
                                fos.write(responseInfo.result.getBytes());
                                fos.close();
                            }
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onStart() {
                        if (callBack != null) {
                            callBack.onStart();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        LogUtil.d(TAG, "Get " + url + " failed:" + msg);
                        printParams(params);
                        boolean findCache = false;
                        // 获取失败，寻找cache
                        FileInputStream fis = null;
                        String cacheContent = "";
                        try {
                            if (!TextUtils.isEmpty(cacheKey)) {
                                String cacheName = md5.generate(cacheKey);

                                File file = new File(CacheConfiguration.getExtCacheDirHttp(null),
                                        cacheName);
                                fis = new FileInputStream(file);
                                byte[] buffer = new byte[10240];
                                int count = fis.read(buffer);
                                StringBuffer sb = new StringBuffer();
                                while (count > 0) {
                                    sb.append(new String(buffer, 0, count));

                                    count = fis.read(buffer);
                                }

                                findCache = true;
                                cacheContent = sb.toString();
                            }
                        } catch (Exception e) {

                        } finally {
                            if (fis != null) {
                                try {
                                    fis.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        if (!findCache) {
                            if (callBack != null) {
                                callBack.onFailure(error, msg);
                            }
                        } else {
                            if (callBack != null) {
                                callBack.onFailure(new HttpExceptionButFoundCache(error),
                                        cacheContent);
                            }
                        }

                    }
                });
    }

    public interface DownloadCallback {
        // 下载成功
        public void onFinish(boolean state, File file);
    }

    public static void download(
            String url, String target, final DownloadCallback callback) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.download(url, target, new RequestCallBack<File>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                if (callback != null) {
                    callback.onFinish(false, null);
                }
            }

            @Override
            public void onSuccess(ResponseInfo<File> arg0) {
                if (callback != null) {
                    callback.onFinish(true, arg0.result);
                }
            }
        });
    }
}
