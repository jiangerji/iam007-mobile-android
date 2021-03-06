package cn.iam007.app.mall.content;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import cn.iam007.app.common.exception.HttpExceptionButFoundCache;
import cn.iam007.app.common.utils.CommonHttpUtils;
import cn.iam007.app.common.utils.ContentUtil;
import cn.iam007.app.common.utils.ContentUtil.BoolCallback;
import cn.iam007.app.common.utils.ImageUtils;
import cn.iam007.app.common.utils.IntentUtil;
import cn.iam007.app.common.utils.PlatformUtils;
import cn.iam007.app.common.utils.logging.LogUtil;
import cn.iam007.app.mall.R;
import cn.iam007.app.mall.base.BaseActivity;
import cn.iam007.app.mall.widget.ShareDialog;
import cn.iam007.app.mall.widget.WebViewWrapper;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

@SuppressLint({ "SetJavaScriptEnabled", "UseSparseArrays" })
public class ContentActivity extends BaseActivity {

    private WebViewWrapper mWebContent;
    private String mContentId = null;
    private String mContentTitle = null;
    private String mContentCategory = null;
    private String mContentBuyUrl = null;
    private String mContentUrl = null;

    private View mThumbnailsRoot = null;

    private View mBuyBtn = null;
    private View mCollectBtn = null; // 关注，取消关注按钮
    private TextView mCollectState = null;
    private boolean mCollected = false; // 是否已经关注

    private ViewPager mContentThumbnails;
    private _ThumbnailsAdapter mThumbnailsAdapter = new _ThumbnailsAdapter();

    private TextView mThumbnailIndex = null; // 表示当前thumbnail的index
    private TextView mContentIntro = null;

    private View mShareBtn = null;// 分享按钮

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_content);

        mWebContent = (WebViewWrapper) findViewById(R.id.webContent);
        initWebView();

        mBuyBtn = findViewById(R.id.goto_buy);
        mBuyBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                IntentUtil.launchWebviewActivity(ContentActivity.this,
                        mContentId, mContentTitle, mContentBuyUrl);
            }
        });
        if (TextUtils.isEmpty(mContentBuyUrl)) {
            mBuyBtn.setEnabled(false);
        }

        mThumbnailsRoot = findViewById(R.id.thumbnails_root);
        LayoutParams layoutParams = mThumbnailsRoot.getLayoutParams();
        if (mContentCategory.equals("8")) {
            layoutParams.width = PlatformUtils.getScreenWidth(this);
            layoutParams.height = (int) (layoutParams.width * 375.0 / 600.0);
        } else {
            layoutParams.width = PlatformUtils.getScreenWidth(this);
            layoutParams.height = layoutParams.width;
        }
        mThumbnailsRoot.setLayoutParams(layoutParams);

        mContentThumbnails = (ViewPager) findViewById(R.id.content_thumbnails);
        mContentThumbnails.setAdapter(mThumbnailsAdapter);
        mContentThumbnails.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                mThumbnailIndex.setText("" + (arg0 + 1) + "/"
                        + mThumbnailsAdapter.getCount());
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                LogUtil.d("ss", "onPageScrollStateChanged:" + state);
                if (state == 1 && mThumbnailsAdapter.getCount() > 1) {
                    disableGestureDetector();
                } else {
                    enableGestureDetector();
                }

            }
        });

        mThumbnailIndex = (TextView) findViewById(R.id.thumbnail_index);
        mContentIntro = (TextView) findViewById(R.id.content_intro);

        mCollectBtn = findViewById(R.id.pd_collect);
        mCollectState = (TextView) findViewById(R.id.pd_collect_state);
        mCollectBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mCollectBtn.setEnabled(false);
                if (mCollected) {
                    mCollectBtn.setSelected(false);
                    mCollectState.setText(R.string.pd_collect);
                    ContentUtil.collect(mContentId, new BoolCallback() {

                        @Override
                        public void onFinish(boolean state, String msg) {
                            mCollectBtn.setEnabled(true);
                            if (state) {
                                mCollected = false;
                            } else {
                                mCollectBtn.setSelected(true);
                            }
                        }
                    });
                } else {
                    mCollectBtn.setSelected(true);
                    mCollectState.setText(R.string.pd_collected);
                    ContentUtil.uncollect(mContentId, new BoolCallback() {

                        @Override
                        public void onFinish(boolean state, String msg) {
                            mCollectBtn.setEnabled(true);
                            if (state) {
                                mCollected = true;
                            } else {
                                mCollectBtn.setSelected(false);
                            }
                        }
                    });
                }
            }
        });
        mShareBtn = findViewById(R.id.pd_share);
        mShareBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ShareDialog dialog = new ShareDialog(ContentActivity.this);
                dialog.show();
            }
        });
    }

    private void initWebView() {

        Intent intent = getIntent();
        mContentId = intent.getStringExtra("id");
        mContentCategory = intent.getStringExtra("category");
        mContentBuyUrl = intent.getStringExtra("buyUrl");
        mContentUrl = intent.getStringExtra("url");
        mContentTitle = intent.getStringExtra("title");

        setTitle(mContentTitle);
        getContentDetail();
    }

    private Handler mHandler = new Handler();

    private void getContentDetail() {
        String action = "detail";
        RequestParams params = new RequestParams("utf-8");
        params.addQueryStringParameter("contentid", mContentId);
        params.addQueryStringParameter("catid", mContentCategory);
        CommonHttpUtils.get(action, params, mCallBack, mContentId, 30);

        // 显示详情网页
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                mWebContent.loadUrl(mContentUrl);
            }
        }, 250);
    }

    private RequestCallBack<String> mCallBack = new RequestCallBack<String>() {

        @Override
        public void onFailure(HttpException error, String msg) {
            if (error instanceof HttpExceptionButFoundCache) {
                parseResult(msg);
            }
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            parseResult(responseInfo.result);
        }
    };

    private void parseResult(String result) {
        try {
            JSONObject object = new JSONObject(result);
            String id = object.optString("i", "");
            if (id.equals(mContentId)) {
                String introText = object.optString("it");
                LogUtil.d(TAG, "introduction:" + introText);
                mContentIntro.setText(introText);

                if (mContentCategory.equals("15")) {
                    mThumbnailsRoot.setVisibility(View.GONE);
                } else {
                    mThumbnailsRoot.setVisibility(View.VISIBLE);
                    JSONArray thumbnailsArray = object.optJSONArray("ts");
                    LogUtil.d(TAG, "thumbnails:");
                    for (int i = 0; i < thumbnailsArray.length(); i++) {
                        LogUtil.d(TAG, "  " + thumbnailsArray.getString(i));
                        //                    mThumbnails.addImageUrl(thumbnailsArray.getString(i));
                        mImageUrls.add(thumbnailsArray.getString(i));
                    }
                    mThumbnailIndex.setText("1/" + thumbnailsArray.length());
                    mThumbnailsAdapter.notifyDataSetChanged();
                }

                String buyUrl = object.optString("b", null);
                if (URLUtil.isNetworkUrl(buyUrl)) {
                    mContentBuyUrl = buyUrl;
                    mBuyBtn.setEnabled(true);
                }
            }
        } catch (Exception e) {
        }

    }

    private ArrayList<String> mImageUrls = new ArrayList<String>();
    private HashMap<Integer, ImageView> mImageViews = new HashMap<Integer, ImageView>();

    private class _ThumbnailsAdapter extends PagerAdapter {

        // 获取要滑动的控件的数量，在这里我们以滑动的广告栏为例，那么这里就应该是展示的广告图片的ImageView数量
        @Override
        public int getCount() {
            return mImageUrls.size();
        }

        // 来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        // PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            view.removeView(mImageViews.get(position));
        }

        // 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            ImageView imageView = mImageViews.get(position);
            if (imageView == null) {
                imageView = new ImageView(view.getContext());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ScaleType.FIT_XY);
                mImageViews.put(position, imageView);

                Log.d("ImageViewPager",
                        "show " + position + " " + mImageUrls.get(position));
                ImageUtils.showImageByUrl(mImageUrls.get(position),
                        imageView,
                        ImageUtils.getOptionsFadeIn());
            }

            view.addView(imageView);
            return imageView;
        }
    }

    @Override
    protected void onFlingRight() {
        super.onFlingRight();
        finish();
    }

}
