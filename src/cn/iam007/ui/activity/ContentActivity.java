package cn.iam007.ui.activity;

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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import cn.iam007.HttpExceptionButFoundCache;
import cn.iam007.R;
import cn.iam007.ui.widget.WebViewWrapper;
import cn.iam007.utils.CommonHttpUtils;
import cn.iam007.utils.ImageUtils;
import cn.iam007.utils.IntentUtil;
import cn.iam007.utils.PlatformUtils;
import cn.iam007.utils.logging.LogUtil;

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

    private View mBuyBtn = null;

    private ViewPager mContentThumbnails;
    private _ThumbnailsAdapter mThumbnailsAdapter = new _ThumbnailsAdapter();

    private TextView mThumbnailIndex = null;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_content);

        mBuyBtn = findViewById(R.id.buy);
        mBuyBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                IntentUtil.launchWebviewActivity(ContentActivity.this,
                        mContentId, mContentTitle, mContentBuyUrl);
            }
        });
        mWebContent = (WebViewWrapper) findViewById(R.id.webContent);
        initWebView();

        View thumbnailsRoot = findViewById(R.id.thumbnails_root);
        LayoutParams layoutParams = thumbnailsRoot.getLayoutParams();
        layoutParams.width = PlatformUtils.getScreenWidth(this);
        layoutParams.height = layoutParams.width;
        thumbnailsRoot.setLayoutParams(layoutParams);

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
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        mThumbnailIndex = (TextView) findViewById(R.id.thumbnail_index);
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
        }, 500);
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

}
