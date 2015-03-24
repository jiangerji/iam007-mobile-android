package cn.iam007.app.common.utils;

import android.content.Context;
import android.content.Intent;
import cn.iam007.app.common.model.ContentInfo;
import cn.iam007.app.common.utils.logging.LogUtil;
import cn.iam007.app.mall.content.ContentActivity;
import cn.iam007.app.mall.product.ProductActivity;
import cn.iam007.app.mall.product.ProductInfo;
import cn.iam007.app.mall.webview.WebViewActivity;

public class IntentUtil {
    private final static String TAG = "intent";

    /**
     * 启动content activity显示内容
     * 
     * @param activity
     * @param contentInfo
     */
    public static void launchContentActivity(
            Context context, ContentInfo contentInfo) {
        Intent intent = new Intent();
        intent.setClass(context, ContentActivity.class);
        intent.putExtra("id", contentInfo.getId());
        intent.putExtra("title", contentInfo.getTitle());
        intent.putExtra("category", contentInfo.getCategory());
        intent.putExtra("url", contentInfo.getUrl());
        intent.putExtra("buyUrl", contentInfo.getBuyUrl());

        LogUtil.d(TAG, "launchContentActivity");
        LogUtil.d(TAG, "  " + intent.toString());

        context.startActivity(intent);
    }

    public static void launchProductActivity(
            Context context, ProductInfo productInfo) {
        Intent intent = new Intent();
        intent.setClass(context, ProductActivity.class);
        intent.putExtra("product", productInfo);

        LogUtil.d(TAG, "launchProductActivity");
        LogUtil.d(TAG, "  " + intent.toString());
        context.startActivity(intent);
    }

    /**
     * 启动webview activity显示内容
     * 
     * @param activity
     * @param contentInfo
     */
    public static void launchWebviewActivity(
            Context context, String contentId, String title, String contentUrl) {
        Intent intent = new Intent();
        intent.setClass(context, WebViewActivity.class);
        intent.putExtra("id", contentId);
        intent.putExtra("title", title);
        intent.putExtra("url", contentUrl);

        LogUtil.d(TAG, "launchWebviewActivity");
        LogUtil.d(TAG, "  " + intent.toUri(Intent.URI_INTENT_SCHEME));
        context.startActivity(intent);
    }
}
