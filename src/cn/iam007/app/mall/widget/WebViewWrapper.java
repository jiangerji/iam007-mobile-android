package cn.iam007.app.mall.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import cn.iam007.app.common.utils.logging.LogUtil;
import cn.iam007.app.mall.R;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewWrapper extends FrameLayout {

    public static final String TAG = "WebViewWrapper";

    private GestureDetector gestureScanner;

    private WebView mWebView;
    private ProgressBar mProgressBar;

    private boolean mUrlCanClick = false;

    public WebViewWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);

        View root = inflate(context, R.layout.webview_wrapper, this);

        mWebView = (WebView) root.findViewById(R.id.webview);
        mProgressBar = (ProgressBar) root.findViewById(R.id.progressBar);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setSupportZoom(false); // 禁止用户对网页进行放大
        mWebView.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        mWebView.setWebViewClient(new _WebViewClient());
        mWebView.setWebChromeClient(new _WebChromeClient());

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

    public void setUrlClickable(boolean clickable) {
        mUrlCanClick = clickable;
    }

    public void loadUrl(String url) {
        if (mWebView != null) {
            mWebView.loadUrl(url);
        }
    }

    public void loadData(String data) {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, data, "text/html", "utf-8", "");
        }
    }

    public boolean canGoBack() {
        boolean result = false;
        if (mWebView != null) {
            result = mWebView.canGoBack();
        }
        return result;
    }

    public void goBack() {
        if (mWebView != null) {
            mWebView.goBack();
        }
    }

    public void refresh() {
        if (mWebView != null) {
            mWebView.reload();
        }
    }

    private class _WebViewClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!mUrlCanClick) {
                return true;
            }

            String redirectUrl = convertUrl(url);
            if (!redirectUrl.equals(url)) {
                view.loadUrl(redirectUrl);
            }
            // 用return false这种方式可以解决，重定向网页导致goBack失效的问题
            // 不能使用view.loadUrl, 否则loadUrl不能使用
            return false;
        }
    }

    private class _WebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(newProgress);
            }
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
