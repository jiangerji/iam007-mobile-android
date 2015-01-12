package cn.iam007.utils;

import org.json.JSONObject;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 对所有文章的一些公共工具类
 * 
 * @author Administrator
 * 
 */
public class ContentUtil {
    public interface BoolCallback {
        public void onFinish(boolean state, String msg);
    }

    /**
     * 获取返回的状态
     * 
     * @param content
     * @return
     */
    public static boolean getState(String content) {
        boolean state = false;

        try {
            JSONObject object = new JSONObject(content);
            if (object.optInt("state", -1) == 0) {
                state = true;
            }
        } catch (Exception e) {
        }

        return state;
    }

    /**
     * 关注某个文章
     * 
     * @param contentid
     *            文章的id
     */
    public static void collect(String contentid, final BoolCallback callback) {
        String uid = PlatformUtils.getUid();
        if (uid == null) {
            // DEBUG
            uid = PlatformUtils.id(null);
        }

        String action = "collect";
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("contentid", contentid);
        params.addQueryStringParameter("uid", uid);
        CommonHttpUtils.get(action, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (callback != null) {
                    String result = responseInfo.result;

                    callback.onFinish(getState(result), "");
                }
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                if (callback != null) {
                    callback.onFinish(false, arg1);
                }
            }
        });
    }

    /**
     * 取消关注某个文章
     * 
     * @param contentid
     *            文章的id
     */
    public static void uncollect(String contentid, final BoolCallback callback) {
        String uid = PlatformUtils.getUid();
        if (uid == null) {
            // DEBUG
            uid = PlatformUtils.id(null);
        }

        String action = "collect";
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("contentid", contentid);
        params.addQueryStringParameter("uid", uid);
        CommonHttpUtils.get(action, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (callback != null) {
                    String result = responseInfo.result;

                    callback.onFinish(getState(result), "");
                }
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                if (callback != null) {
                    callback.onFinish(false, arg1);
                }
            }
        });
    }
}
