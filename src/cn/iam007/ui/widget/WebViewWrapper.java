package cn.iam007.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import cn.iam007.utils.logging.LogUtil;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewWrapper extends WebView {

    public static final String TAG = "WebViewWrapper";

    private GestureDetector gestureScanner;

    public WebViewWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);

        getSettings().setJavaScriptEnabled(true);
        getSettings().setUseWideViewPort(true);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setSupportZoom(false); // 禁止用户对网页进行放大

        setWebViewClient(new MyWebViewClient());

        gestureScanner = new GestureDetector(context,
                new GestureDetector.OnGestureListener() {

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return false;
                    }

                    @Override
                    public void onShowPress(MotionEvent e) {

                    }

                    @Override
                    public boolean onScroll(
                            MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
                        return false;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {

                    }

                    @Override
                    public boolean onFling(
                            MotionEvent e1, MotionEvent e2, float velocityX,
                            float velocityY) {
                        return false;
                    }

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return false;
                    }
                });
        gestureScanner.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            public boolean onDoubleTap(MotionEvent e) {
                return false;
            }

            public boolean onDoubleTapEvent(MotionEvent e) {
                //双击时产生两次
                LogUtil.v("test", "onDoubleTapEvent");
                return true;
            }

            public boolean onSingleTapConfirmed(MotionEvent e) {
                //短快的点击算一次单击
                LogUtil.v("test", "onSingleTapConfirmed");
                return false;
            }
        });
    }

    //    @Override
    //    public boolean onTouchEvent(MotionEvent ev) {
    // // 可以解决双击放大的问题
    //        if (gestureScanner.onTouchEvent(ev)) {
    //            return false;
    //        } else {
    //            return super.onTouchEvent(ev);
    //        }
    //    }

    private class MyWebViewClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(convertUrl(url));
            // 用return false这种方式可以解决，重定向网页导致goBack失效的问题
            // 不能使用view.loadUrl, 否则loadUrl不能使用
            return false;
        }
    }

    /**
     * 将url转换为移动端的url
     * 
     * @param url
     * @return
     */
    private String convertUrl(String url) {
        String result = url;
        try {
            if (url.contains("item.jd.com")) {
                // 将京东的web url转换为移动端的url
                int lastIndex = url.lastIndexOf(".");
                int startIndex = url.lastIndexOf("/");
                String id = url.substring(startIndex + 1, lastIndex);

                result = String.format("http://m.jd.com/product/%s.html", id);
            }
        } catch (Exception e) {
        }

        return result;
    }

}
