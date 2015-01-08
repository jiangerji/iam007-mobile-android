package cn.iam007.utils;

import android.app.Activity;
import android.content.Intent;
import cn.iam007.model.ContentInfo;
import cn.iam007.ui.activity.ContentActivity;

public class IntentUtil {

    /**
     * 启动activity显示内容
     * 
     * @param activity
     * @param contentInfo
     */
    public static void launchContentActivity(
            Activity activity, ContentInfo contentInfo) {
        Intent intent = new Intent();
        intent.setClass(activity, ContentActivity.class);
        intent.putExtra("id", contentInfo.getId());
        intent.putExtra("title", contentInfo.getTitle());
        intent.putExtra("category", contentInfo.getCategory());
        intent.putExtra("url", contentInfo.getUrl());
        intent.putExtra("buyUrl", contentInfo.getBuyUrl());

        activity.startActivity(intent);
    }
}
