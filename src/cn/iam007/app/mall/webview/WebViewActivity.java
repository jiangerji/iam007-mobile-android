package cn.iam007.app.mall.webview;

import android.content.Intent;
import android.os.Bundle;
import cn.iam007.app.common.utils.logging.LogUtil;
import cn.iam007.app.mall.R;
import cn.iam007.app.mall.base.BaseActivity;
import cn.iam007.app.mall.widget.WebViewWrapper;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;

public class WebViewActivity extends BaseActivity {

    private String mContentUrl = null;
    private WebViewWrapper mWebContent;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_webview);
        mWebContent = (WebViewWrapper) findViewById(R.id.webContent);
        initWebView();
    }

    private void initWebView() {
        mWebContent.setUrlClickable(true);

        Intent intent = getIntent();
        mContentUrl = intent.getStringExtra("url");

        setTitle(intent.getStringExtra("title"));

        mWebContent.loadUrl(mContentUrl);
        LogUtil.d("WebView", "loadUrl:" + mContentUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.add("refresh");
        menuItem.setIcon(R.drawable.refresh_content_btn)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
                        | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        menuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mWebContent.refresh();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (mWebContent.canGoBack()) {
            mWebContent.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onFlingRight() {
        super.onFlingRight();

        if (mWebContent.canGoBack()) {
            mWebContent.goBack();
        } else {
            finish();
        }
    }

    @Override
    protected void onFlingLeft() {
        if (mWebContent.canGoForward()) {
            mWebContent.goForward();
        }
    }

}
