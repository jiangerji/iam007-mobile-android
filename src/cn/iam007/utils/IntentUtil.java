package cn.iam007.utils;

import android.content.Context;
import android.content.Intent;
import cn.iam007.model.ContentInfo;
import cn.iam007.ui.activity.ContentActivity;
import cn.iam007.ui.activity.WebViewActivity;

public class IntentUtil {

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

        context.startActivity(intent);
    }
}
