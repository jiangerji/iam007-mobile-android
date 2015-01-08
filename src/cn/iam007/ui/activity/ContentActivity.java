package cn.iam007.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import cn.iam007.R;
import cn.iam007.ui.widget.WebViewWrapper;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;

@SuppressLint("SetJavaScriptEnabled")
public class ContentActivity extends BaseActivity {

    private WebViewWrapper mWebContent;
    private String mContentId = null;
    private String mContentCategory = null;
    private String mContentUrl = null;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_content);

        mWebContent = (WebViewWrapper) findViewById(R.id.webContent);
        initWebView();
    }

    private void initWebView() {

        Intent intent = getIntent();
        mContentId = intent.getStringExtra("id");
        mContentCategory = intent.getStringExtra("category");
        mContentUrl = intent.getStringExtra("url");

        setTitle(intent.getStringExtra("title"));

        mWebContent.loadUrl(mContentUrl);
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
}
